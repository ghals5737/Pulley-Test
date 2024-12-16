package org.example.pulleyapi.problem.domain.response

import org.example.pulleyapi.problem.domain.Problem

data class GetProblemResponse(
    val problemList: List<Problem>
)
