FROM openjdk:8-jdk-alpine
EXPOSE 7080
COPY api/target/app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]