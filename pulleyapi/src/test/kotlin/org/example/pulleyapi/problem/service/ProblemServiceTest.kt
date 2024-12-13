package org.example.pulleyapi.problem.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class ProblemServiceTest @Autowired constructor(
    private val problemService: ProblemService
) {

    @Test
    fun `사용할수있는 문제가 충분한경우(난이도 LOW)`() {
        val level = "LOW"
        val totalCount = 10
        val difficultyCounts = listOf(10, 10, 10) // 하: 10개, 중: 10개, 상: 10개

        val result = problemService.getDistributionByLevel(level, totalCount, difficultyCounts)

        val expected = mapOf(
            "LOW" to 5L,
            "MIDDLE" to 3L,
            "HIGH" to 2L
        )
        assertEquals(expected, result)
    }

    @Test
    fun `사용할수있는 문제가 충분한경우(난이도 HIGH)`() {
        val level = "HIGH"
        val totalCount = 10
        val difficultyCounts = listOf(3, 3, 3) // 하: 3개, 중: 3개, 상: 3개

        val result = problemService.getDistributionByLevel(level, totalCount, difficultyCounts)

        val expected = mapOf(
            "LOW" to 3L,
            "MIDDLE" to 3L,
            "HIGH" to 3L
        ) // 부족한 경우 다른 난이도에서 채움
        assertEquals(expected, result)
    }

    @Test
    fun `사용할수있는 문제가 충분한경우(난이도 MIDDLE)`() {
        val level = "MIDDLE"
        val totalCount = 12
        val difficultyCounts = listOf(2, 4, 3) // 하: 2개, 중: 4개, 상: 3개

        val result = problemService.getDistributionByLevel(level, totalCount, difficultyCounts)

        val expected = mapOf(
            "LOW" to 2L,
            "MIDDLE" to 4L,
            "HIGH" to 3L
        ) // 부족한 문제는 다른 난이도에서 최대한 채움
        assertEquals(expected, result)
    }

    @Test
    fun `사용가능한 문제와 요구한 문제수가  정확히 맞을경우`() {
        val level = "LOW"
        val totalCount = 10
        val difficultyCounts = listOf(5, 3, 2) // 하: 5개, 중: 3개, 상: 2개

        val result = problemService.getDistributionByLevel(level, totalCount, difficultyCounts)

        val expected = mapOf(
            "LOW" to 5L,
            "MIDDLE" to 3L,
            "HIGH" to 2L
        ) // 모든 문제가 정확히 맞음
        assertEquals(expected, result)
    }

    @Test
    fun `문제수가 부족한 경우 LOW난이도 부터 추가 할당한다`() {
        val level = "HIGH"
        val totalCount = 15
        val difficultyCounts = listOf(10, 10, 10) // 하: 10개, 중: 10개, 상: 10개

        val result = problemService.getDistributionByLevel(level, totalCount, difficultyCounts)

        val expected = mapOf(
            "LOW" to 4L,
            "MIDDLE" to 4L,
            "HIGH" to 7L
        ) // 하 난이도를 우선적으로 할당
        assertEquals(expected, result)
    }



}