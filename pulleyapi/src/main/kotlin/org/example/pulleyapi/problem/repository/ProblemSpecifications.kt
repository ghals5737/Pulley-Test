package org.example.pulleyapi.problem.repository
import org.example.pulleyapi.problem.entity.ProblemEntity
import org.example.pulleyapi.problem.entity.ProblemType
import org.springframework.data.jpa.domain.Specification;

class ProblemSpecifications {
    fun hasUnitCode(unitCodes:List<String>):Specification<ProblemEntity>{
        return Specification{ root, _, _ ->
            root.get<String>("unitCode").`in`(unitCodes)
        }
    }

    fun hasProblemType(problemType:ProblemType):Specification<ProblemEntity>{
        return Specification{ root, _, criteriaBuilder  ->
            if(problemType==ProblemType.ALL){
                return@Specification criteriaBuilder.conjunction()
            }
            root.get<ProblemType>("problemType").`in`(problemType)
        }
    }

}