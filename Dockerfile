FROM openjdk:8-jdk-alpine
EXPOSE 7080
COPY target/app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]