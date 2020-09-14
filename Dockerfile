FROM gcr.io/modern-lms/lms-baseimage

COPY . .
ADD entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
EXPOSE 8888

CMD ["java", "-jar", "starter-1.0-SNAPSHOT.jar"]