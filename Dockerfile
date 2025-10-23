FROM maven:3.9.6-eclipse-temurin-17

# Set environment variables
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
ENV BASE_URL=https://www.automationexercise.com

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml /app/

# Download dependencies (cached layer)
RUN mvn -B -q -e -DskipTests dependency:resolve

# Copy source code and resources
COPY src /app/src

# Copy .env.example as .env (so dotenv can load it)
COPY .env.example /app/.env

# Install Playwright browsers
RUN mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium" || true

# Run tests
CMD ["mvn", "-B", "test"]
