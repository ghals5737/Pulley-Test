package org.example.pulleyapi.teacher.repository

import org.example.pulleyapi.teacher.entity.TeacherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository<TeacherEntity, Long> {
}