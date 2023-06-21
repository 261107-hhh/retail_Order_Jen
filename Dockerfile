FROM openjdk:17
EXPOSE 9004
ADD target/retail-order-docker.jar retail-order-docker.jar 
ENTRYPOINT ["java", "-jar","/retail-order-docker.jar"]