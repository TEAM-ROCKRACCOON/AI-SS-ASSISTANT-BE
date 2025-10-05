FROM amazoncorretto:21

# 보안: non-root
RUN useradd -m appuser
WORKDIR /app

# 앱 바이너리만
COPY ./build/libs/aiss-server-0.0.1-SNAPSHOT.jar ./aiss-application.jar

ENV TZ=Asia/Seoul
USER appuser

CMD ["java", "-jar", "aiss-application.jar"]
