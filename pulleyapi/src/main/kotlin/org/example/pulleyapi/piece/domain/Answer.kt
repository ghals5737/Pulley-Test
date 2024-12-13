package org.example.pulleyapi.piece.domain

data class Answer(
    val pieceId: Long,
    val studentId: Long,
    val problemId: Long,
    val answer: String
)
