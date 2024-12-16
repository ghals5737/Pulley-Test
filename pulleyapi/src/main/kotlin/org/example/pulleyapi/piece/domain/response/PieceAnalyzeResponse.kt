package org.example.pulleyapi.piece.domain.response

import org.example.pulleyapi.piece.domain.ProblemAnalyze
import org.example.pulleyapi.piece.domain.StudentAnalyze

data class PieceAnalyzeResponse(
    val pieceId: Long,
    val pieceTitle : String,
    val studentAnalyzes : List<StudentAnalyze>,
    val problemAnalyzes : List<ProblemAnalyze>
)
