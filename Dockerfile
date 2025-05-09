# 1) Build Stage : Gradle로 JAR 생성
FROM gradle:7.6-jdk17 AS builder

#캐시 활용을 위한 디렉토리 설정
WORKDIR /home/gradle/project

#gradle wrapper와 설정 파일 우선 복사 -> 캐시 활용
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

#의존성만 다운로드(캐시용)
RUN ./gradlew --no-daemon dependencies

#실제 소스 복사 후 빌드
COPY . .
RUN ./gradlew --no-daemon clean bootJar

# 2) Run Stage : OpenJDK로 JAR 실행
FROM openjdk:17-jdk-slim

WORKDIR /app

# 1단계에서 만든 JAR파일을 복사
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# 환경변수 파일(.env)은 docker-compose.yml의 env_file로 읽히므로
# 여기선 따로 저장할 필요가 없음

# 메타 정보
LABEL authors="deokkyu"

# prod 프로필으로 애플리케이션 실행
ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=prod -jar app.jar"]