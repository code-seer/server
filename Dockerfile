FROM gcr.io/learnet/learnet-base

COPY api/target/learnet-starter-dist.jar .
ADD entrypoint.sh entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 7080

CMD ["java", "-jar", "learnet-starter-dist.jar"]