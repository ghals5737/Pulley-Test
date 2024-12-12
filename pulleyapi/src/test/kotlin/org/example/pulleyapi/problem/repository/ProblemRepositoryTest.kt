package org.example.pulleyapi.problem.repository

import org.example.pulleyapi.problem.entity.ProblemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertEquals

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql("/sql/data.sql")
class ProblemRepositoryTest @Autowired constructor(
    private val problemRepository: ProblemRepository
) {

    @Test
    fun `유형코드와 문제유형으로 필터링된 난이도별 문제 개수를 조회한다`() {
        // given: 테스트 데이터 및 조건
        val unitCodes = listOf("uc1506","uc1507","uc1511","uc1510")
        val problemType = ProblemType.ALL

        // 예상 결과
        val expectedLowCount = 1L
        val expectedMiddleCount = 18L
        val expectedHighCount = 3L

        // when: 쿼리 실행
        val result = problemRepository.getProblemCountsByDifficulty(unitCodes, problemType)

        // then: 결과 검증
        assertEquals(3, result.size)
        assertEquals(expectedLowCount, result[0])
        assertEquals(expectedMiddleCount, result[1])
        assertEquals(expectedHighCount, result[2])
    }

    @Test
    fun `유형코드와 문제유형으로 필터링된 난이도별 문제를 랜덤하게 조회한다`() {
        // given: 테스트 데이터 및 조건
        val unitCodes = listOf("uc1506","uc1507","uc1511","uc1510")
        val problemType = ProblemType.ALL
        val lowLevels= listOf(1)
        val middleLevels= listOf(2, 3, 4)
        val highLevels= listOf(5)
        val limitCnt = 5

        // when: 쿼리 실행
        val lowDifficultyProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, lowLevels, limitCnt)
        val middleDifficultyProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, middleLevels, limitCnt)
        val highDifficultyProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, highLevels, limitCnt)

        // then: 결과 검증
        assertEquals(1, lowDifficultyProblems.size)
        assertEquals(5, middleDifficultyProblems.size)
        assertEquals(3, highDifficultyProblems.size)
    }
}
