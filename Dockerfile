FROM gcr.io/modern-lms/lms-baseimage

COPY . .
ADD entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
EXPOSE 8081

CMD ["mvn", "spring-boot:run"]