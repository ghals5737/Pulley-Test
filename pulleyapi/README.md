# Pulley-Test
## 발생할 수 있는 문제와 해결 방안

### 1. **데이터 무결성**

#### **위험요소**

- 동시 요청이 많을 경우 데이터 무결성이 깨질 가능성:
    - 예: `assignmentPiece` 메서드에서 여러 요청이 동시에 들어오면 같은 학생에게 학습지가 여러 번 할당될 수 있음.
- `gradingPiece` 메서드에서 동시 요청이 많을 경우 상태가 제대로 갱신되지 않을 위험.

#### **해결 방안**

- 비관적 락(Pessimistic Lock)
    - 데이터가 업데이트 될때까지 LOCK을 걸어 다른 트렌젝션의 접근을 막음

- 트랜잭션 격리 수준 설정
    - 필요한 경우 트랜잭션 격리 수준을 `REPEATABLE_READ` 또는 `SERIALIZABLE`로 설정하여 데이터 일관성을 유지.

------

### 2. **대량 데이터 처리 시 성능 문제**

#### **위험요소**

- `gradingPiece`와 `getProblemsByPieceId`는 학습지에 포함된 문제가 많을 경우 많은 오버헤드 발생.

#### **해결 방안**

- 페이징 처리
    - 문제가 많은 학습지를 처리할 때는 페이징을 적용해 필요한 데이터만 로드.
- 캐싱
    - 학습지문제의 경우 자주 조회되고 변경은 자주 발생하지 않아서 캐싱(`Redis`)을 적용해도 괜찮아보임

------

### 3. **잘못된 요청 데이터**

#### **위험요소**

- 클라이언트에서 잘못된 데이터가 들어왔을 경우 서버가 예외를 제대로 처리하지 못할 가능성.
- 예: `assignmentPiece`에서 `studentIds`가 빈 값이거나, 잘못된 형식의 데이터가 들어오는 경우.

#### **해결 방안**

- 입력 데이터 유효성 검사

    - `@Valid`와 `Bean Validation`을 사용해 입력 데이터를 검증.
    - 예: `PieceAssignmentRequest`와 같은 DTO에 `@NotEmpty`, `@Min`, `@Max` 등의 검증 어노테이션 추가.

- Global Exception Handler

    - Spring의 `@ControllerAdvice`를 활용하여 공통 예외 처리 로직 작성.

    - 클라이언트에게 명확한 에러 메시지를 반환하고, 서버가 안정적으로 동작하도록 예외를 처리.

------

### 5. **확장성 문제**

#### **위험요소**

- 학습지 문제 수가 많거나, 할당 대상 학생이 많을 경우, 성능 문제가 발생할 가능성이 있음.
- 여러 선생님과 학생이 동시에 대량의 학습지를 생성하거나 할당하면, DB 부하가 증가.

#### **해결 방안**

- 비동기 처리

  대량 데이터를 처리하는 메서드(`assignmentPiece`)에서 비동기 작업(`@Async`)을 도입해 응답 시간을 단축.

- 데이터베이스 인덱스 작업

  테이블 인덱스를 추가하여 조회 속도를 개선.

    - `unitCode`와 `problemType`에 복합 인덱스를 추가.

- 분산 시스템

  규모 데이터를 처리해야 하는 경우, 데이터베이스 샤딩 또는 분산 캐싱(`Redis`) 도입.

  `kafka,rabbitmq`같은 메시지큐를 도입하여 부하가 걸리는작업은 따로 워커서버에서 처리하도록 함

------
