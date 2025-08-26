# Amazon Corretto 17 기반 이미지 사용
FROM openjdk:17

# 환경변수는 컨테이너 실행 시점에 주입 (docker run -e ...)사용
# Spring Boot Profile 설정 (dev)
ENV SPRING_PROFILES_ACTIVE=dev

# 컨테이너에서 사용할 포트 설정
EXPOSE 8080

# 작업 디렉토리 설정
WORKDIR /app
# JAR 파일 복사
COPY ./build/libs/*.jar ./app.jar

# 비루트 사용자 생성 및 전환
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser
# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
