package org.example.pulleyapi.piece.domain.request

import org.example.pulleyapi.piece.domain.Answer

data class GrandingRequest(
    val studentId: Long,
    val answers: List<Answer>
)
