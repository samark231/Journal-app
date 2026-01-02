# ------------------------------------------------------------------
# STAGE 1: BUILD (Using Ubuntu/Jammy for stable networking)
# ------------------------------------------------------------------
# Changed from 'alpine' to 'jammy' to fix the wget timeout
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# 1. Copy dependency definitions
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 2. Give execution permission
RUN chmod +x mvnw

# 3. Download dependencies
# This uses standard Linux networking, so it won't crash like Alpine
RUN ./mvnw dependency:go-offline

# 4. Copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ------------------------------------------------------------------
# STAGE 2: RUN (Using a slim JRE)
# ------------------------------------------------------------------
# We also switch the runner to Jammy for consistency
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]