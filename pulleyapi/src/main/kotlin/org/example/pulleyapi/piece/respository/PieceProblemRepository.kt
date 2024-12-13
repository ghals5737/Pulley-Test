package org.example.pulleyapi.piece.respository

import org.example.pulleyapi.piece.entity.PieceProblemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PieceProblemRepository : JpaRepository<PieceProblemEntity, Long> {
}