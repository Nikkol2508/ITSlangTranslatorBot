FROM azul/zulu-openjdk-alpine:11
EXPOSE 8080
ADD target/ITSlang-0.0.1-SNAPSHOT.jar /app.jar
CMD java -jar /app.jar
