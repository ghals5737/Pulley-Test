package org.example.pulleyapi.piece.entity

import jakarta.persistence.*
import org.example.pulleyapi.piece.entity.PieceEntity
import org.example.pulleyapi.student.entity.StudentEntity
import org.example.pulleyapi.teacher.entity.TeacherEntity

@Entity
@Table(name = "assignments")
class AssignmentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var isCompleted: Boolean = false,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: StudentEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    var teacher: TeacherEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", nullable = false)
    var piece: PieceEntity,
){
    fun updateIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }
}