FROM ssm:base

EXPOSE 8080

WORKDIR /app
COPY . .
RUN chmod -R 777 mvnw
RUN ./mvnw -DskipTests clean package

ENTRYPOINT ["java","-jar","/app/target/mss-0.0.1-SNAPSHOT.jar"]
