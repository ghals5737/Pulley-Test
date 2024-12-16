package org.example.pulleyapi.problem.service

import org.example.pulleyapi.problem.domain.DifficultyCount
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
        val availableCounts = listOf(
            DifficultyCount(1, 10L),
            DifficultyCount(2, 10L),
            DifficultyCount(3, 10L)
        )

        val result = problemService.getDistributionByLevel(level, totalCount, availableCounts)

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
        val availableCounts = listOf(
            DifficultyCount(1, 3L),
            DifficultyCount(2, 3L),
            DifficultyCount(3, 3L)
        )

        val result = problemService.getDistributionByLevel(level, totalCount, availableCounts)

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
        val availableCounts = listOf(
            DifficultyCount(1, 2L),
            DifficultyCount(2, 4L),
            DifficultyCount(3, 3L)
        )

        val result = problemService.getDistributionByLevel(level, totalCount, availableCounts)

        val expected = mapOf(
            "LOW" to 2L,
            "MIDDLE" to 4L,
            "HIGH" to 3L
        )
        assertEquals(expected, result)
    }

    @Test
    fun `사용가능한 문제와 요구한 문제수가  정확히 맞을경우`() {
        val level = "LOW"
        val totalCount = 10
        val availableCounts = listOf(
            DifficultyCount(1, 5L),
            DifficultyCount(2, 3L),
            DifficultyCount(3, 2L)
        )

        val result = problemService.getDistributionByLevel(level, totalCount, availableCounts)

        val expected = mapOf(
            "LOW" to 5L,
            "MIDDLE" to 3L,
            "HIGH" to 2L
        )
        assertEquals(expected, result)
    }

    @Test
    fun `문제수가 부족한 경우 LOW난이도 부터 추가 할당한다`() {
        val level = "HIGH"
        val totalCount = 15
        val availableCounts = listOf(
            DifficultyCount(1, 10L),
            DifficultyCount(2, 10L),
            DifficultyCount(3, 10L)
        )

        val result = problemService.getDistributionByLevel(level, totalCount, availableCounts)

        val expected = mapOf(
            "LOW" to 4L,
            "MIDDLE" to 4L,
            "HIGH" to 7L
        )
        assertEquals(expected, result)
    }



}