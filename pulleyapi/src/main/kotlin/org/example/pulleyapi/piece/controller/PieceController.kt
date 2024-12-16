package org.example.pulleyapi.piece.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.pulleyapi.piece.domain.request.GrandingRequest
import org.example.pulleyapi.piece.domain.request.PieceAssignmentRequest
import org.example.pulleyapi.piece.domain.request.PieceCreateRequest
import org.example.pulleyapi.piece.domain.response.AssignmentPieceResponse
import org.example.pulleyapi.piece.domain.response.GradingPieceResponse
import org.example.pulleyapi.piece.domain.response.PieceAnalyzeResponse
import org.example.pulleyapi.piece.domain.response.PieceCreateResponse
import org.example.pulleyapi.piece.service.PieceService
import org.example.pulleyapi.problem.domain.Problem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/piece")
@Tag(name = "Piece API", description = "학습지 관리 API")
class PieceController(
    private val pieceService: PieceService
) {

    @PostMapping("")
    @Operation(
        summary = "학습지 생성",
        description = "새로운 학습지를 생성합니다.",
    )
    fun createPiece(
        @RequestBody pieceCreateRequest: PieceCreateRequest
    ): ResponseEntity<PieceCreateResponse> {
        return ResponseEntity.ok(
            pieceService.createPiece(pieceCreateRequest)
        )
    }

    @PostMapping("/{pieceId}")
    @Operation(
        summary = "학습지 할당",
        description = "특정 학습지를 학생들에게 할당합니다.",
    )
    fun assignmentPiece(
        @Parameter(description = "할당할 학습지 ID", required = true)
        @PathVariable("pieceId") pieceId: Long,
        @Parameter(description = "할당할 학생들의 ID 리스트", required = true)
        @RequestParam("studentIds") studentIds: String,
        @RequestBody pieceAssignmentRequest: PieceAssignmentRequest
    ): ResponseEntity<AssignmentPieceResponse> {
        return ResponseEntity.ok(
            pieceService.assignmentPiece(pieceAssignmentRequest, pieceId, studentIds)
        )
    }

    @GetMapping("/problems")
    @Operation(
        summary = "학습지 문제 조회",
        description = "특정 학습지에 포함된 문제들을 조회합니다.",
    )
    fun getProblemsByPieceId(
        @Parameter(description = "조회할 학습지 ID", required = true)
        @RequestParam("pieceId") pieceId: Long
    ): ResponseEntity<List<Problem>> {
        return ResponseEntity.ok(
            pieceService.getProblemsByPieceId(pieceId)
        )
    }

    @PutMapping("/problems")
    @Operation(
        summary = "학습지 채점",
        description = "특정 학습지를 채점하고 결과를 저장합니다.",
    )
    fun gradingPiece(
        @Parameter(description = "채점할 학습지 ID", required = true)
        @RequestParam("pieceId") pieceId: Long,
        @RequestBody grandingRequest: GrandingRequest
    ): ResponseEntity<GradingPieceResponse> {
        return ResponseEntity.ok(pieceService.gradingPiece(pieceId, grandingRequest))
    }

    @GetMapping("/analyze")
    @Operation(
        summary = "학습지 분석",
        description = "특정 학습지의 학생과 문제 통계를 조회합니다."
    )
    fun getPieceAalyze(
        @Parameter(description = "분석할 학습지 ID", required = true)
        @RequestParam("pieceId") pieceId: Long,
    ) : ResponseEntity<PieceAnalyzeResponse> {
        return ResponseEntity.ok(
            pieceService.getPieceAalyze(pieceId)
        )
    }
}
