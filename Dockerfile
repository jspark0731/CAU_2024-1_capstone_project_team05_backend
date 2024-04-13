FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22
WORKDIR /app
COPY *.jar /app/spring-boot-application.jar
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]
