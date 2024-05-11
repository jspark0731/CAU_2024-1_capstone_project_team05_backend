# GraalVM을 기반으로 빌드 아티팩트를 생성
FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-21 as build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN ./gradlew build -x test

# 런타임에도 GraalVM 이미지를 사용하여 네이티브 이미지 기능을 활
FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]
