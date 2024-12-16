package org.example.pulleyapi.piece.domain.response

import org.example.pulleyapi.piece.domain.GradingResult

data class GradingPieceResponse(
    val pieceId: Long,
    val studentId: Long,
    val results: List<GradingResult>
)