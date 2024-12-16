package org.example.pulleyapi.piece.service

import org.example.pulleyapi.piece.domain.Answer
import org.example.pulleyapi.piece.domain.request.GrandingRequest
import org.example.pulleyapi.piece.domain.request.PieceAssignmentRequest
import org.example.pulleyapi.piece.domain.request.PieceCreateRequest
import org.example.pulleyapi.piece.domain.response.AssignmentPieceResponse
import org.example.pulleyapi.piece.domain.response.GradingPieceResponse
import org.example.pulleyapi.piece.domain.response.PieceAnalyzeResponse
import org.example.pulleyapi.piece.domain.response.PieceCreateResponse
import org.example.pulleyapi.problem.domain.Problem

interface PieceService {
    fun createPiece(pieceCreateRequest: PieceCreateRequest): PieceCreateResponse
    fun assignmentPiece(pieceAssignmentRequest: PieceAssignmentRequest, pieceId: Long, studentIds:String): AssignmentPieceResponse
    fun getProblemsByPieceId(pieceId: Long): List<Problem>
    fun gradingPiece(pieceId: Long, grandingRequest: GrandingRequest) : GradingPieceResponse
    fun getPieceAalyze(pieceId: Long): PieceAnalyzeResponse
}