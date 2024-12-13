package org.example.pulleyapi.problem.service

import org.example.pulleyapi.problem.repository.ProblemRepository
import org.springframework.stereotype.Service

@Service
class ProblemServiceImpl(
    private val problemRepository: ProblemRepository
): ProblemService {
    override fun getDistributionByLevel(level:String,totalCount:Int,difficultyCounts: List<Int>): Map<String, Long> {
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

        val availableCounts = listOf(
            difficultyCounts[0].toLong(),
            difficultyCounts[1].toLong(),
            difficultyCounts[2].toLong()
        )

        val distribution = mutableListOf<Long>(0, 0, 0)

        for (i in requestedDistribution.indices) {
            distribution[i] = minOf(requestedDistribution[i], availableCounts[i])
        }

        var shortage = totalCount - distribution.sum()
        var idx=0

        while(shortage > 0){
            val additional = minOf(shortage, availableCounts[idx] - distribution[idx])
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