package org.example.pulleyapi.piece.domain.response

data class PieceCreateResponse(
    val pieceId:Long,
    val title:String,
    val problemIds:List<Long>,
    val teacherId:Long
)
