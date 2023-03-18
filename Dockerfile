FROM openjdk:11
EXPOSE 8081
ADD target/dish-recipe-management.jar dish-recipe-management.jar
ENTRYPOINT ["java" , "-jar" , "/dish-recipe-management.jar"]
