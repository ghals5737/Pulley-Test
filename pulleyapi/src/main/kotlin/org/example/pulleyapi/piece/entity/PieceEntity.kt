package org.example.pulleyapi.piece.entity

import jakarta.persistence.*
import org.example.pulleyapi.teacher.entity.TeacherEntity

@Entity
@Table(name = "pieces")
class PieceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var title: String = "",
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    var teacher: TeacherEntity = TeacherEntity(),
) {
}