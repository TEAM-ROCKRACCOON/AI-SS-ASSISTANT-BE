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
RUN useradd -m appuser
USER appuser
COPY --from=builder /workspace/build/libs/app.jar /app/app.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]