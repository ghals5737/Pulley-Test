package org.example.pulleyapi.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "problems")
class ProblemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var unitCode: String,
    @Enumerated(EnumType.STRING)
    var problemType: ProblemType,
    var level: Int,
    var answer: String,
){

}