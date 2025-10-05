# ---------- 1) Build stage ----------
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace
COPY gradle gradle
COPY gradlew .
COPY settings.gradle build.gradle ./
COPY . .
RUN ./gradlew --no-daemon clean bootJar -x test

# ---------- 2) Runtime stage ----------
FROM amazoncorretto:21
WORKDIR /app

# 비루트 UID로 실행 (이름 없는 UID여도 OK)
RUN mkdir -p /app && chown -R 10001:0 /app
USER 10001

COPY --from=builder /workspace/build/libs/app.jar /app/app.jar

ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
