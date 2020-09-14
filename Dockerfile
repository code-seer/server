FROM gcr.io/modern-lms/lms-baseimage

COPY . .
ADD entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
EXPOSE 8081

CMD ["cd", "api", "&&", "mvn", "spring-boot:run"]