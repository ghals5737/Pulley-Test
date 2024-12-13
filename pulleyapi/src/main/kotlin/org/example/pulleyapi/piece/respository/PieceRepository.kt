package org.example.pulleyapi.piece.respository

import org.example.pulleyapi.piece.entity.PieceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PieceRepository : JpaRepository<PieceEntity, Long> {

}