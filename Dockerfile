FROM gcr.io/modern-lms/lms-baseimage

COPY api/target/lms-starter-dist.jar .
ADD entrypoint.sh entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 7080

CMD ["java", "-jar", "lms-starter-dist.jar"]