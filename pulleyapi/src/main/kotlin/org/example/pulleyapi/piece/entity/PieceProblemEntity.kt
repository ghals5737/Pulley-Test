package org.example.pulleyapi.piece.entity

import jakarta.persistence.*
import jakarta.persistence.Entity
import org.example.pulleyapi.problem.entity.ProblemEntity

@Entity
class PieceProblemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", nullable = false)
    var piece: PieceEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: ProblemEntity,
) {
}