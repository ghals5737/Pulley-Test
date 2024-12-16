package org.example.pulleyapi.teacher.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.pulleyapi.teacher.domain.Teacher

@Entity
@Table(name = "teachers")
class TeacherEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String = "",
) {
    companion object {
        fun from(teacher: Teacher): TeacherEntity {
            return TeacherEntity(
                id = teacher.id,
                name = teacher.name
            )
        }
    }
}