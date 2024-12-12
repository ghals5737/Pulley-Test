package org.example.pulleyapi.problem.service

interface ProblemService {
    fun getDistributionByLevel(): Map<String, Long>
}