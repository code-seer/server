FROM gcr.io/modern-lms/lms-baseimage

COPY api/target/lms-starter-dist.jar .
ADD entrypoint.sh entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8081

CMD ["java", "-jar", "lms-starter-dist.jar"]