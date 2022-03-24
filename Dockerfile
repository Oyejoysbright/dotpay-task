FROM openjdk:15-jdk-alpine
ARG JAR_FILE=target/*.jar
EXPOSE 8080
LABEL maintainer "dotpay-challenge"
RUN addgroup -S dotpay && adduser -S dotpay -G dotpay
USER dotpay:dotpay
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]