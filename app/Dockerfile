# ---- builder ----
FROM gradle:8.8-jdk21 AS builder

WORKDIR /workspace

# Gradle 캐시 최적화를 위해 build.gradle / settings.gradle 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY app/build.gradle app/build.gradle
COPY domain/build.gradle domain/build.gradle
COPY infra/build.gradle infra/build.gradle

# 의존성만 미리 받아오기 (빌드 속도 향상)
RUN gradle dependencies --no-daemon || true

# 이후 전체 복사
COPY . .

# Spring Boot jar 빌드 (app 모듈 기준)
RUN gradle clean :app:bootJar --no-daemon

# ---- Runtime ----
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# 보안상 non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# builder에서 jar만 복사
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

# healthcheck를 위한 엔드포인트 기준 실행
HEALTHCHECK CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]