package org.example.pulleyapi.problem.entity

import jakarta.persistence.*
import org.example.pulleyapi.teacher.domain.Teacher

@Entity
@Table(name = "problems")
class ProblemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var unitCode: String = "",
    @Enumerated(EnumType.STRING)
    var problemType: ProblemType = ProblemType.ALL,
    var level: Int = 0,
    var answer: String = "",
    val randId: Int= 0,
){

}