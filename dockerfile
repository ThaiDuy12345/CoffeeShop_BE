FROM eclipse-temurin:20

COPY ./target/Main_CoffeeShop-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]

# build command: docker build -t coffee-shop-app .
# run command: docker run -p 8080:8080 --name=coffee-shop-app coffee-shop-app 