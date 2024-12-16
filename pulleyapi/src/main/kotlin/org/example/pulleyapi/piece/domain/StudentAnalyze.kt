package org.example.pulleyapi.piece.domain

data class StudentAnalyze(
    val studentId: Long,
    val correctAnswers : Long,
    val totalAnswers : Long,
    val correctRate : Double
)
