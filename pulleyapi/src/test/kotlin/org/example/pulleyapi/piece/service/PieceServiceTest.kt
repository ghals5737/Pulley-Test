package org.example.pulleyapi.piece.service

import org.example.pulleyapi.piece.domain.Answer
import org.example.pulleyapi.piece.domain.request.GrandingRequest
import org.example.pulleyapi.piece.domain.request.PieceAssignmentRequest
import org.example.pulleyapi.piece.domain.request.PieceCreateRequest
import org.example.pulleyapi.piece.entity.AnswerEntity
import org.example.pulleyapi.piece.entity.AssignmentEntity
import org.example.pulleyapi.piece.entity.PieceEntity
import org.example.pulleyapi.piece.entity.PieceProblemEntity
import org.example.pulleyapi.piece.respository.AnswerRepository
import org.example.pulleyapi.piece.respository.AssignmentRepository
import org.example.pulleyapi.piece.respository.PieceProblemRepository
import org.example.pulleyapi.piece.respository.PieceRepository
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.student.entity.StudentEntity
import org.example.pulleyapi.teacher.entity.TeacherEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Sql("/sql/piece-test.sql")
@Transactional
class PieceServiceTest @Autowired constructor(
    private val pieceService: PieceService,
    private val pieceRepository: PieceRepository,
    private val pieceProblemRepository: PieceProblemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val answerRepository: AnswerRepository,
) {

    @Test
    fun `학습지를 생성하면 학습지 정보를 반환해야 한다`() {
        // Given
        val teacher = TeacherEntity(11,name = "TeacherA")

        val request = PieceCreateRequest(
            title = "Math Quiz",
            teacherId = teacher.id!!,
            problemIds = listOf(1L,2L,3L,4L)
        )

        // When
        val response = pieceService.createPiece(request)

        // Then
        val savedPiece = pieceRepository.findById(response.pieceId).get()
        assertEquals("Math Quiz", savedPiece.title)
        assertEquals(teacher.id, savedPiece.teacher.id)
    }

    @Test
    fun `학습지에 학생들을 할당하면 할당된 학생 목록을 반환해야 한다`() {
        // Given
        val teacher = TeacherEntity(11,name = "TeacherA")

        val piece = pieceRepository.save(PieceEntity(
            title = "Science Quiz",
            teacher = teacher
        ))

        val studentIds = "11,12"
        val expectedIds=studentIds.split(",").map { it.toLong() }
        val request = PieceAssignmentRequest(teacherId = teacher.id!!)

        // When
        val response = pieceService.assignmentPiece(request, piece.id!!, studentIds)

        // Then
        val assignments = assignmentRepository.findAll()
        assertEquals(2, assignments.size)
        assertEquals(expectedIds, response.assignmentStudents)
    }

    @Test
    fun `학습지를 채점하면 정답 데이터가 저장되고 할당 상태가 완료로 변경되어야 한다`() {
        // Given
        val teacher = TeacherEntity(11,name = "TeacherA")

        val piece = pieceRepository.save(PieceEntity(
            title = "Math Quiz",
            teacher = teacher
        ))

        val student1 = StudentEntity(11L,name = "StudentA")

        val problem1 = ProblemEntity(1L, answer = "A");
        val problem2 = ProblemEntity(2L, answer = "B")

        pieceProblemRepository.saveAll(listOf(
            PieceProblemEntity(piece = piece, problem = problem1),
            PieceProblemEntity(piece = piece, problem = problem2)
        ))

        val assignment = assignmentRepository.save(AssignmentEntity(
            isCompleted = false,
            student = student1,
            teacher = teacher,
            piece = piece
        ))

        val gradingRequest = GrandingRequest(
            studentId = teacher.id!!,
            answers = listOf(
                Answer(problemId = problem1.id!!, answer = "A"),
                Answer(problemId = problem2.id!!, answer = "B")
            )
        )

        // When
        pieceService.gradingPiece(piece.id!!, gradingRequest)

        // Then
        val updatedAssignment = assignmentRepository.findById(assignment.id!!).get()
        assertTrue(updatedAssignment.isCompleted)
        val answers = answerRepository.findAll()
        assertEquals(2, answers.size)
        assertTrue(answers.all { it.isCorrect })
    }

    @Test
    fun `학습지 통계를 조회하면 학생과 문제에 대한 통계 데이터를 반환해야 한다`() {
        // Given
        val teacher = TeacherEntity(1,name = "TeacherA")

        val piece = pieceRepository.save(PieceEntity(
            title = "History Quiz",
            teacher = teacher
        ))

        val student1 = StudentEntity(11L,name = "StudentA")

        val problem1 = ProblemEntity(1L, answer = "A");
        val problem2 = ProblemEntity(2L, answer = "B")

        pieceProblemRepository.saveAll(listOf(
            PieceProblemEntity(piece = piece, problem = problem1),
            PieceProblemEntity(piece = piece, problem = problem2)
        ))

        answerRepository.saveAll(listOf(
            AnswerEntity(piece=piece,problem = problem1, student = student1, isCorrect = true),
            AnswerEntity(piece=piece,problem = problem2, student = student1, isCorrect = false)
        ))

        // When
        val response = pieceService.getPieceAalyze(piece.id!!)

        // Then
        assertEquals(1, response.studentAnalyzes.size)
        assertEquals(50.0, response.studentAnalyzes.first().correctRate)
        assertEquals(2, response.problemAnalyzes.size)
    }
}
