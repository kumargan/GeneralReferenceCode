FROM java:8  

COPY target/webskt-0.1.0.jar app.jar

EXPOSE 8080 

CMD ["java","-Xmx300M","-Xms256M","-jar","app.jar"]  
