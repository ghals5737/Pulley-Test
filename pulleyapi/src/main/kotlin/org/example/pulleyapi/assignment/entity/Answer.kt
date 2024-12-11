package org.example.pulleyapi.assignment.entity

import jakarta.persistence.*
import org.example.pulleyapi.piece.entity.PieceEntity
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.student.entity.StudentEntity


@Entity
@Table(name = "answers")
class Answer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var isCorrect: Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", nullable = false)
    var piece: PieceEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: ProblemEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: StudentEntity,
){

}