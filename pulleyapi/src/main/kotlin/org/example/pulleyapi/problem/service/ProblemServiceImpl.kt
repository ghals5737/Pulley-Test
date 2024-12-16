package org.example.pulleyapi.problem.service

import org.example.pulleyapi.problem.domain.DifficultyCount
import org.example.pulleyapi.problem.entity.ProblemType
import org.example.pulleyapi.problem.repository.ProblemRepository
import org.springframework.stereotype.Service
import org.example.pulleyapi.problem.domain.Problem
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemServiceImpl(
    private val problemRepository: ProblemRepository
): ProblemService {


    @Transactional(readOnly = true)
    override fun getProblems(totalCount: Int, unitCodeList: String, level: String, problemType: ProblemType): List<Problem> {
        val unitCodes = unitCodeList.split(",").map { it.trim() }
        val counts = problemRepository.getAvailableCountsByDifficulty(unitCodes, problemType)

        val defaultDifficulties = listOf(
            DifficultyCount(1, 0),
            DifficultyCount(2, 0),
            DifficultyCount(3, 0)
        )

        val availableCounts=defaultDifficulties.map { default ->
            counts.find { it.difficulty == default.difficulty } ?: default
        }

        val distribution = getDistributionByLevel(level, totalCount, availableCounts)

        val lowProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, listOf(1), distribution["LOW"])
        val middleProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, listOf(2, 3, 4), distribution["MIDDLE"])
        val highProblems = problemRepository.getRandProblemsFilteredByDifficulty(unitCodes, problemType, listOf(5), distribution["HIGH"])

        val problems = lowProblems + middleProblems + highProblems
        return problems.map { Problem(it.id!!,it.answer, it.unitCode, it.level, it.problemType) }
    }


    override fun getDistributionByLevel(level:String,totalCount:Int,availableCounts: List<DifficultyCount>): Map<String, Long> {
        val requestedDistribution = when (level) {
            "LOW" -> listOf(
                (totalCount * 0.5).toLong(),
                (totalCount * 0.3).toLong(),
                (totalCount * 0.2).toLong()
            )
            "MIDDLE" -> {
                listOf(
                    (totalCount * 0.25).toLong(),
                    (totalCount * 0.5).toLong(),
                    (totalCount * 0.25).toLong()
                )
            }
            "HIGH" -> listOf(
                (totalCount * 0.2).toLong(),
                (totalCount * 0.3).toLong(),
                (totalCount * 0.5).toLong()
            )
            else -> throw IllegalArgumentException("Invalid level: $level")
        }

        val distribution = mutableListOf<Long>(0, 0, 0)

        for (i in requestedDistribution.indices) {
            distribution[i] = minOf(requestedDistribution[i], availableCounts[i].count)
        }

        var shortage = totalCount - distribution.sum()
        var idx=0

        while(shortage > 0 && idx < distribution.size) {
            val additional = minOf(shortage, availableCounts[idx].count - distribution[idx])
            distribution[idx] += additional
            shortage -= additional
            idx+=1
        }

        return mapOf(
            "LOW" to distribution[0],
            "MIDDLE" to distribution[1],
            "HIGH" to distribution[2]
        )
    }


}