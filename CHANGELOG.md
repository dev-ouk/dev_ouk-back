# 🧾 CHANGELOG

📚 [주차별 개발 로그 보러가기](./docs/devlog/)

## 2025년 10월

### 4주차 (10/27 ~ 11/02)
- ✅ `AppProperties` 테스트 Validation 바인딩 수정
- 🧩 Gradle Daemon Start 지연 문제 해결
- 🧰 `@SpringBootTest` Profile 분리 (`test` → `app-test.yml`)

### 3주차 (10/20 ~ 10/26)
- ⚙️ ECR/ECS 배포 파이프라인 초안
- 🧪 GitHub Actions 워크플로 통합 (`build → test → docker`)
- 🔍 Jacoco 70% 커버리지 기준 설정

### 2주차 (10/13 ~ 10/19)
- 🧠 Redis Cache 추가 및 `@Cacheable` 도입
- 🧾 Logback MDC 필터 기반 TraceID 구현

### 1주차 (10/23 ~ 10/30)
- [2025.10.30] infraWiringConfig and Builder for Infra
- [2025.10.30] test application.yml에 AppProperties 바인딩을 위한 값 추가
- [2025.10.30] AppProperties 기본 설정
- [2025.10.30] spotless apply
- [2025.10.30] application.yml 분리
- [2025.10.29] ActuatorHealthTest spotlessApply
- [2025.10.29] actuator / prometheus / micrometer settings
- [2025.10.29] Add PostgreSQL container tests with Testcontainers
- [2025.10.29] Add test for checking correlation ID
- [2025.10.29] jdeps - Architecture check
- [2025.10.29] Member Domain Test
- [2025.10.28] 기존 더미 코드 삭제 및 테스트 코드 추가
- [2025.10.28] 전역 에러 처리
- [2025.10.28] app 부분 Member 도메인
- [2025.10.28] inMemory 기반 레포지토리 임시
- [2025.10.28] Domain에 Member 도메인 추가
- [2025.10.28] Transactional 사용을 위한 spring-tx 추가.
- [2025.10.28] spotlessCheck에 주석 파일도 걸려서 제거
- [2025.10.28] CorrelationIdFilterTest 추가
- [2025.10.27] github action ci.yml 작성
- [2025.10.27] logback json 포맷 및 Correlation-id 필터
- [2025.10.26] local docker-compose
- [2025.10.25] Multi-stage Dockerfile
- [2025.10.24] jacoco 각 모듈 단위 라인 테스트 커버리지 70% 설정
- [2025.10.24] DiffPlug spotless(googleJavaFormat) & checkstyle 적용
- [2025.10.23] Spotless로 코드 형식 check & Apply
- [2025.10.23] scanBasePackages로 app/domain/infra 전부 스캔
- [2025.10.23] ArchTest를 통한 모듈 내부 클래스 의존성 테스트
- [2025.10.23] app/domain/infra 모듈 분리

