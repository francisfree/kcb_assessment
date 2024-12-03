# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17-alpine  AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src

RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM public.ecr.aws/docker/library/eclipse-temurin:17-jre-alpine

WORKDIR /opt/app
RUN addgroup --system -g 2000 javauser && adduser -S -s /usr/sbin/nologin -G javauser -u 2000 javauser
COPY --from=build /app/target/*.jar app.jar
RUN chown -R javauser:javauser .
USER javauser
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]