# Constitution: Java Board System

## 1. Core Principles
- **Language**: Java 21 (Record, Pattern Matching, Virtual Threads 등 최신 기능 적극 활용)
- **Framework**: Spring Boot 3.4+
- **Database**: MySQL 8.0+
- **Architecture**: Layered Architecture (Controller - Service - Repository - Entity/DTO)

## 2. Coding Standards
- **Naming**:
    - Class: PascalCase
    - Method/Variable: camelCase
    - DB Table/Column: snake_case
- **Standardization**: 모든 API 응답은 일관된 `ApiResponse<T>` 래퍼 클래스를 사용한다.
- **DTO**: Request/Response 객체는 Java `record`를 사용하여 불변성을 보장한다.

## 3. Persistence Layer
- **JPA**: Spring Data JPA를 사용하며, 복잡한 쿼리는 Querydsl 또는 JPQL을 활용한다.
- **Transaction**: 비즈니스 로직(Service)에는 `@Transactional`을 명시한다.

## 4. Testing
- 모든 핵심 비즈니스 로직은 JUnit 5와 AssertJ를 이용한 단위 테스트를 포함해야 한다.
- Controller 테스트는 `MockMvc`를 활용한다.