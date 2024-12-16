package org.example.pulleyapi.piece.domain.response

data class AssignmentPieceResponse(
    val pieceId: Long,
    val assignmentStudents: List<Long>
)
