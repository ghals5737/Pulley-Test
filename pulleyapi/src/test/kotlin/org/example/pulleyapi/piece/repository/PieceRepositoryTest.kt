package org.example.pulleyapi.piece.repository

import jakarta.transaction.Transactional
import org.example.pulleyapi.piece.domain.Answer
import org.example.pulleyapi.piece.entity.AssignmentEntity
import org.example.pulleyapi.piece.entity.PieceEntity
import org.example.pulleyapi.piece.entity.PieceProblemEntity
import org.example.pulleyapi.piece.respository.AssignmentRepository
import org.example.pulleyapi.piece.respository.PieceProblemRepository
import org.example.pulleyapi.piece.respository.PieceRepository
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.problem.entity.ProblemType
import org.example.pulleyapi.student.entity.StudentEntity
import org.example.pulleyapi.teacher.entity.TeacherEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql("/sql/piece-repository-test.sql")
class PieceRepositoryTest @Autowired constructor(
    private val pieceRepository: PieceRepository,
    private val pieceProblemRepository: PieceProblemRepository,
    private val assignmentRepository: AssignmentRepository
){
    @Test
    @Transactional
    fun `학습지를 저장하고 조회한다`() {
        //given
        val teacher = TeacherEntity(name = "TeacherA")
        val problems= listOf(
            ProblemEntity(1, "MATH01", ProblemType.SUBJECTIVE, 1, "Answer1", 123),
            ProblemEntity(2, "MTH01", ProblemType.SUBJECTIVE, 2, "Answer2",456),
            ProblemEntity(3, "SCI01", ProblemType.SELECTION, 3, "Answer3",789),
            ProblemEntity(4, "SCI01", ProblemType.SUBJECTIVE, 4, "Answer4",123123)
        )


        // Create a piece
        val piece = PieceEntity(name = "Test Piece", teacher = teacher)
        val savedPiece = pieceRepository.save(piece)

        // Link problems to the piece
        val pieceProblems = problems.map { problem ->
            PieceProblemEntity(piece = savedPiece, problem = problem)
        }
        pieceProblemRepository.saveAll(pieceProblems)

        // Verify saved data
        val foundPiece = pieceRepository.findById(savedPiece.id!!)
        assertTrue(foundPiece.isPresent)
        assertEquals("Test Piece", foundPiece.get().name)
        assertEquals("TeacherA", foundPiece.get().teacher.name)

//        val pieceProblemEntities = pieceProblemRepository.findAll()
//        assertEquals(2, pieceProblemEntities.size)
//        assertEquals(savedPiece.id, pieceProblemEntities[0].piece.id)
//        assertTrue(pieceProblemEntities.any { it.problem.answer == "Answer1" })
//        assertTrue(pieceProblemEntities.any { it.problem.answer == "Answer2" })
    }

    @Test
    fun `학생이 특정 학습지를 이미 받았는지 확인한다`() {
        // Given
        val teacher = TeacherEntity(1L,name = "Teacher A")
        val student = StudentEntity(1L,name = "Student A")
        val piece = PieceEntity(1L,name = "Test Piece", teacher = teacher)

        assignmentRepository.save(
            AssignmentEntity(
                isCompleted = false,
                student = student,
                teacher = teacher,
                piece = piece
            )
        )

        // When
        val exists = assignmentRepository.existsByStudent_IdAndPiece_Id(student.id!!, piece.id!!)

        // Then
        assertTrue(exists)
    }

    @Test
    fun `학생이 학습지를 받지 않은 경우 false를 반환한다`() {
        // Given
        val teacher = TeacherEntity(1,name = "Teacher A")
        val student = StudentEntity(1,name = "Student A")
        val piece = PieceEntity(1,name = "Test Piece", teacher = teacher)

        // When
        val exists = assignmentRepository.existsByStudent_IdAndPiece_Id(student.id!!, piece.id!!)

        // Then
        assertFalse(exists)
    }

    @Test
    fun `다수의 학생에게 학습지를 할당하고 이미 있는 학생은 제외한다`() {
        val teacher = TeacherEntity(1, name = "Teacher A")
        val students = listOf(
            StudentEntity(1, name = "StudentA"),
            StudentEntity(2, name = "StudentB"),
            StudentEntity(3, name = "StudentC")
        )
        val piece = PieceEntity(1, name = "Test Piece", teacher = teacher)

        val existingAssignment = AssignmentEntity(
            isCompleted = false,
            student = students[1], // StudentB
            teacher = teacher,
            piece = piece
        )

        assignmentRepository.save(existingAssignment)
        val studentIds= listOf(1L, 2L, 3L)
        val alreadyAssignedStudentIds = assignmentRepository.findStudentIdsByPieceIdAndStudentIdIn(piece.id!!, studentIds)

        val newAssignments = students.filter { it.id!! !in alreadyAssignedStudentIds }.map { student ->
            AssignmentEntity(
                isCompleted = false,
                student = student,
                teacher = teacher,
                piece = piece
            )
        }

        // When
        val savedAssignments = assignmentRepository.saveAll(newAssignments)

        // Then
        assertEquals(2, savedAssignments.size)
        val assignedStudentIds = savedAssignments.map { it.student.id }
        assertTrue(assignedStudentIds.contains(1L))
        assertTrue(assignedStudentIds.contains(3L))
        assertFalse(assignedStudentIds.contains(2L))
    }

    @Test
    fun `채점 요청이 올바르게 처리된다`() {
        // Given
        val teacher = TeacherEntity(1, name = "Teacher A")
        val student = StudentEntity(1, name = "StudentA")
        val piece = PieceEntity(1, name = "Test Piece", teacher = teacher)
        val problems= listOf(
            ProblemEntity(1, "MATH01", ProblemType.SUBJECTIVE, 1, "Answer1", 123),
            ProblemEntity(2, "MTH01", ProblemType.SUBJECTIVE, 2, "Answer2",456),
            ProblemEntity(3, "SCI01", ProblemType.SELECTION, 3, "Answer3",789),
            ProblemEntity(4, "SCI01", ProblemType.SUBJECTIVE, 4, "Answer4",123123)
        )

        val answers = listOf(
            Answer(1,1,1,"Answer1"),
            Answer(1,1,2,"Answer2"),
            Answer(1,1,3,"Answer1"),
            Answer(1,1,4,"Answer1"),
        )

        // When
        //val results = gradingService.gradeAnswers(piece.id!!, student.id!!, gradingRequests)

        // Then
//        assertEquals(2, results.size)
//        assertTrue(results.any { it.problemId == problems[0].id && it.isCorrect })
//        assertTrue(results.any { it.problemId == problems[1].id && !it.isCorrect })
    }

}