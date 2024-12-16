package org.example.pulleyapi.problem.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.pulleyapi.problem.domain.response.GetProblemResponse
import org.example.pulleyapi.problem.entity.ProblemType
import org.example.pulleyapi.problem.service.ProblemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems")
@Tag(name = "Piece API", description = "문제 API")
class ProblemController(
    private val problemService: ProblemService
)
{

    @GetMapping("/")
    @Operation(
        summary = "문제 조회",
        description = "문제코드와 난이도 문제 유형으로 필터링된 문제 목록을 조회한다.",
    )
    fun getProblems(
        @RequestParam("totalCount") totalCount: Int,
        @RequestParam("unitCodeList") unitCodeList: String,
        @RequestParam("level") level: String,
        @RequestParam("problemType") problemType: ProblemType,

    ): ResponseEntity<GetProblemResponse> {
        return ResponseEntity.ok(GetProblemResponse(problemService.getProblems(totalCount, unitCodeList, level, problemType)))
    }
}