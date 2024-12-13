package org.example.pulleyapi.problem.service

interface ProblemService {
    fun getDistributionByLevel(level:String,totalCount:Int,difficultyCounts: List<Int>): Map<String, Long>
}