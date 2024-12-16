package org.example.pulleyapi.piece.respository

import org.example.pulleyapi.piece.entity.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AnswerRepository : JpaRepository<AnswerEntity, Long> {
    @Query("""
        SELECT a.student.id, COUNT(CASE WHEN a.isCorrect = true THEN 1 END), COUNT(a)
        FROM AnswerEntity a
        WHERE a.piece.id = :pieceId
        GROUP BY a.student.id
    """)
    fun findStudentStatisticsByPieceId(@Param("pieceId") pieceId: Long): List<Array<Long>>

    @Query("""
        SELECT a.problem.id, COUNT(CASE WHEN a.isCorrect = true THEN 1 END), COUNT(a)
        FROM AnswerEntity a
        WHERE a.piece.id = :pieceId
        GROUP BY a.problem.id
    """)
    fun findProblemStatisticsByPieceId(@Param("pieceId") pieceId: Long): List<Array<Long>>
}