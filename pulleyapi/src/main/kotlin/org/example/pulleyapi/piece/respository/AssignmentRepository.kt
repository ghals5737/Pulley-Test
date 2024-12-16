package org.example.pulleyapi.piece.respository

import org.example.pulleyapi.piece.entity.AssignmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AssignmentRepository : JpaRepository<AssignmentEntity,Long>{
    fun existsByStudent_IdAndPiece_Id(studentId:Long, pieceId: Long): Boolean
    @Query("""
    SELECT a.student.id 
    FROM AssignmentEntity a 
    WHERE a.piece.id = :pieceId AND a.student.id IN :studentIds
""")
    fun findStudentIdsByPieceIdAndStudentIdIn(
        @Param("pieceId") pieceId: Long,
        @Param("studentIds") studentIds: List<Long>
    ): List<Long>

    fun findByPieceIdAndStudentId(pieceId: Long, studentId: Long): AssignmentEntity
}