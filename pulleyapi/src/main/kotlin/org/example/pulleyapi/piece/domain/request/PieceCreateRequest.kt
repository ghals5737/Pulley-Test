package org.example.pulleyapi.piece.domain.request

import org.example.pulleyapi.teacher.domain.Teacher

data class PieceCreateRequest(
    val teacherId: Long,
    val title:String,
    val problemIds:List<Long>
)
