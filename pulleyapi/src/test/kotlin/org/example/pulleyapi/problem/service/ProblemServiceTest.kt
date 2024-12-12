package org.example.pulleyapi.problem.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class ProblemServiceTest @Autowired constructor(
    private val problemService: ProblemService
) {

    @Test
    fun `난이도에 따라 문제 분포를 한다`(){
        // given
        // when
        val result = problemService.getDistributionByLevel()
        // then
        println(result)
    }



}