package org.example.pulleyapi.piece.respository

import org.example.pulleyapi.piece.entity.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<AnswerEntity, Long> {
}