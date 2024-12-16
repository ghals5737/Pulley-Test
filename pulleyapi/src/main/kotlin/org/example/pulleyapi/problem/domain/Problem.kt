package org.example.pulleyapi.problem.domain

import org.example.pulleyapi.problem.entity.ProblemType

data class Problem(
    val id: Long,
    var answer: String,
    var unitCode: String,
    var level: Int,
    var problemType: ProblemType
)
