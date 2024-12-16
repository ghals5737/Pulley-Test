package org.example.pulleyapi.problem.service

import org.example.pulleyapi.problem.domain.DifficultyCount
import org.example.pulleyapi.problem.domain.Problem
import org.example.pulleyapi.problem.entity.ProblemType

interface ProblemService {
    fun getDistributionByLevel(level:String,totalCount:Int,availableCounts: List<DifficultyCount>): Map<String, Long>
    fun getProblems(totalCount: Int, unitCodeList: String, level: String, problemType: ProblemType):List<Problem>
}