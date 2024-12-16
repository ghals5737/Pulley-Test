package org.example.pulleyapi.piece.domain

data class ProblemAnalyze (
    val problemId: Long,
    val correctAnswers : Long,
    val totalAnswers : Long,
    val correctRate : Double
)