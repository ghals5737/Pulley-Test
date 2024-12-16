package org.example.pulleyapi.piece.service

import org.springframework.transaction.annotation.Transactional
import org.example.pulleyapi.piece.domain.Answer
import org.example.pulleyapi.piece.domain.GradingResult
import org.example.pulleyapi.piece.domain.ProblemAnalyze
import org.example.pulleyapi.piece.domain.StudentAnalyze
import org.example.pulleyapi.piece.domain.request.GrandingRequest
import org.example.pulleyapi.piece.domain.request.PieceAssignmentRequest
import org.example.pulleyapi.piece.domain.request.PieceCreateRequest
import org.example.pulleyapi.piece.domain.response.AssignmentPieceResponse
import org.example.pulleyapi.piece.domain.response.GradingPieceResponse
import org.example.pulleyapi.piece.domain.response.PieceAnalyzeResponse
import org.example.pulleyapi.piece.domain.response.PieceCreateResponse
import org.example.pulleyapi.piece.entity.AnswerEntity
import org.example.pulleyapi.piece.entity.AssignmentEntity
import org.example.pulleyapi.piece.entity.PieceEntity
import org.example.pulleyapi.piece.entity.PieceProblemEntity
import org.example.pulleyapi.piece.respository.AnswerRepository
import org.example.pulleyapi.piece.respository.AssignmentRepository
import org.example.pulleyapi.piece.respository.PieceProblemRepository
import org.example.pulleyapi.piece.respository.PieceRepository
import org.example.pulleyapi.problem.domain.Problem
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.student.entity.StudentEntity
import org.example.pulleyapi.teacher.entity.TeacherEntity
import org.example.pulleyapi.teacher.repository.TeacherRepository
import org.springframework.stereotype.Service

@Service
class PieceServiceImpl (
    private val pieceRepository: PieceRepository,
    private val pieceProblemRepository: PieceProblemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val answerRepository: AnswerRepository,
    private val teacherRepository: TeacherRepository,
): PieceService {

    @Transactional
    override fun createPiece(pieceCreateRequest: PieceCreateRequest): PieceCreateResponse {
        val pieceEntity=pieceRepository.save(PieceEntity(
            title = pieceCreateRequest.title,
            teacher = TeacherEntity(id = pieceCreateRequest.teacherId),
        ))
        val pieceProblems=pieceCreateRequest.problemIds.map {
            PieceProblemEntity(
                piece = pieceEntity,
                problem= ProblemEntity(id = it)
            )
        }
        pieceProblemRepository.saveAll(pieceProblems)
        return PieceCreateResponse(
            pieceEntity.id!!,
            pieceEntity.title,
            pieceCreateRequest.problemIds,
            pieceCreateRequest.teacherId,
        )
    }

    @Transactional
    override fun assignmentPiece(pieceAssignmentRequest: PieceAssignmentRequest,pieceId: Long, studentIdsStr: String): AssignmentPieceResponse {
        val studentIds=studentIdsStr.split(",").map { it.toLong() }
        pieceRepository.findById(pieceId).map {
            it ->
            if(it.teacher.id!=pieceAssignmentRequest.teacherId){
                throw IllegalArgumentException("Teacher with ID ${pieceAssignmentRequest.teacherId} is not the owner of piece with ID $pieceId")
            }
        }.orElseThrow {
            throw IllegalArgumentException("Piece with ID $pieceId not found")
        }
        val alreadyAssignedStudentIds = assignmentRepository.findStudentIdsByPieceIdAndStudentIdIn(pieceId, studentIds)

        val newAssignments = studentIds
            .filter { !alreadyAssignedStudentIds.contains(it) }
            .map { studentId -> AssignmentEntity(
                null,
                false,
                StudentEntity(id=studentId),
                TeacherEntity(id=pieceAssignmentRequest.teacherId),
                PieceEntity(id=pieceId)
                )
            }
        val assignments=assignmentRepository.saveAll(newAssignments)

        return AssignmentPieceResponse(
            pieceId = pieceId,
            assignmentStudents=assignments.map { it.student.id!! }
        )
    }

    @Transactional(readOnly = true)
    override fun getProblemsByPieceId(pieceId: Long): List<Problem> {
        return pieceProblemRepository.findProblemsByPieceId(pieceId).map{
            Problem(
                id = it.problem.id!!,
                answer = it.problem.answer,
                unitCode = it.problem.unitCode,
                level = it.problem.level,
                problemType = it.problem.problemType,
            )
        }
    }

    @Transactional
    override fun gradingPiece(pieceId: Long, grandingRequest: GrandingRequest): GradingPieceResponse {
        val student = StudentEntity(id = grandingRequest.studentId)

        val gradingAnswers = pieceProblemRepository.findProblemsByPieceId(pieceId).map { pieceProblemEntity ->
            val isCorrect = grandingRequest.answers.any {
                it.problemId == pieceProblemEntity.problem.id && it.answer == pieceProblemEntity.problem.answer
            }

            AnswerEntity(
                id = null,
                isCorrect = isCorrect,
                piece = pieceProblemEntity.piece,
                problem = pieceProblemEntity.problem,
                student = student
            )
        }
        val assignment = assignmentRepository.findByPieceIdAndStudentId(pieceId, student.id!!)
        assignment.updateIsCompleted(true)

        answerRepository.saveAll(gradingAnswers)
        return GradingPieceResponse(
            pieceId = pieceId,
            studentId = student.id!!,
            results = gradingAnswers.map {
                GradingResult(
                    problemId = it.problem.id!!,
                    isCorrect = it.isCorrect
                )
            }
        )
    }

    @Transactional(readOnly = true)
    override fun getPieceAalyze(pieceId: Long):PieceAnalyzeResponse {
        val piece = pieceRepository.findById(pieceId).orElseThrow() { IllegalArgumentException("Piece with ID $pieceId not found") }

        val studentAnalyzes = answerRepository.findStudentStatisticsByPieceId(pieceId).map { row ->
            StudentAnalyze(
                row[0] ,
                row[1] ,
                row[2] ,
                (row[1]).toDouble() / (row[2]).toInt() * 100
            )
        }

        val problemAnalyzes = answerRepository.findProblemStatisticsByPieceId(pieceId).map { row ->
            ProblemAnalyze(
                row[0],
                row[1],
                row[2],
                (row[1]).toDouble() / (row[2]).toInt() * 100
            )

        }

        return PieceAnalyzeResponse(
            piece.id!!,
            piece.title,
            studentAnalyzes,
            problemAnalyzes
        )
    }

}