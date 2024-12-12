package org.example.pulleyapi.problem.repository

import org.example.pulleyapi.problem.domain.DifficultyCount
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.problem.entity.ProblemType
import org.springframework.beans.factory.parsing.Problem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface ProblemRepository : JpaRepository<ProblemEntity,Long>{
    @Query("""
        SELECT COUNT(p.level)
            FROM ProblemEntity p
            WHERE p.unitCode IN :unitCodes
              AND (p.problemType = :problemType OR :problemType = 'ALL')
            GROUP BY CASE
                WHEN level = 1 THEN 1
                WHEN level BETWEEN 2 AND 4 THEN 2
                WHEN level = 5 THEN 3
            END
            ORDER BY CASE
                WHEN level = 1 THEN 1
                WHEN level BETWEEN 2 AND 4 THEN 2
                WHEN level = 5 THEN 3
            END
        """)
    fun getProblemCountsByDifficulty(
        @Param("unitCodes") unitCodes: List<String>,
        @Param("problemType") problemType: ProblemType
    ): List<Long>

    @Query("""
        SELECT p
            FROM ProblemEntity p
            WHERE p.unitCode IN :unitCodes
              AND (p.problemType = :problemType OR :problemType = 'ALL')
              AND p.level IN :levels
              AND p.randId > FLOOR((RAND()*10000000))
            ORDER BY p.randId 
            limit :limitCnt
        """)
    fun getRandProblemsFilteredByDifficulty(
        @Param("unitCodes") unitCodes: List<String>,
        @Param("problemType") problemType: ProblemType,
        @Param("levels") levels: List<Int>,
        @Param("limitCnt") limitCnt: Int
    ): List<ProblemEntity>
}