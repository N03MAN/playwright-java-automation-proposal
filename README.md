# Playwright Java Automation Framework

A comprehensive test automation framework using Playwright for Java with API and UI testing capabilities.

## ✅ **Current Status: Production Ready!**

- ✅ **21 API Tests** - Fully functional and passing
- ⚠️ **6 UI Tests** - Framework complete (run separately: `mvn test -Dtest="LoginUITests"`)
- ✅ **All Assignment Requirements Met**
- ✅ **CI/CD Pipeline Configured**
- ✅ **Docker Support Ready**

**Quick Start:** Run `mvn test` to execute API tests, then `mvn allure:serve` for reports.

## 🚀 Features

- **Playwright** for modern browser automation
- **TestNG** for test organization and execution
- **REST Assured** for API testing
- **Allure** reporting for beautiful test reports
- **Docker** support for containerized execution
- **Environment-based configuration** using `.env` files
- Page Object Model (POM) design pattern

## 📋 Prerequisites

- Java 17+
- Maven 3.6+
- Docker (optional, for containerized execution)

## 🔧 Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd playwright-java-automation-proposal
```

### 2. Configure environment variables

Create a `.env` file in the project root (or copy from `.env.example`):

```bash
cp .env.example .env
```

Edit `.env` to set your configuration:

```properties
BASE_URL=https://www.automationexercise.com
```

### 3. Install dependencies

```bash
mvn clean install
```

### 4. Install Playwright browsers

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## 🧪 Running Tests

### Local Execution

Run all tests:
```bash
mvn test
```

Run specific test suites:
```bash
# API tests only
mvn test -Dtest="*APITests"

# UI tests only
mvn test -Dtest="*UITests"
```

Run with custom BASE_URL:
```bash
mvn test -DBASE_URL=https://staging.example.com
```

### Docker Execution

Build the Docker image:
```bash
docker build -t playwright-tests .
```

Run tests in Docker:
```bash
docker run --rm playwright-tests
```

Run with custom environment variables:
```bash
docker run --rm -e BASE_URL=https://staging.example.com playwright-tests
```

## 📁 Project Structure

```
playwright-java-automation-proposal/
├── src/test/java/
│   ├── api/                    # API client classes
│   │   └── UserApiClient.java
│   ├── base/                   # Base test classes
│   │   └── BaseTest.java
│   ├── pages/                  # Page Object Model classes
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   └── RegistrationPage.java
│   ├── tests/
│   │   ├── api/               # API tests
│   │   │   ├── LoginAPITests.java
│   │   │   └── RegistrationAPITests.java
│   │   └── ui/                # UI tests
│   │       ├── LoginUITests.java
│   │       └── RegistrationUITests.java
│   └── utils/                 # Utility classes
│       ├── ConfigManager.java
│       ├── DataUtils.java
│       └── TestListeners.java
├── src/test/resources/
│   ├── testdata/              # Test data files
│   │   ├── logins.json
│   │   └── users.json
│   ├── config.properties
│   ├── allure.properties
│   └── log4j2.xml
├── .env                       # Environment variables (not in git)
├── .env.example              # Environment variables template
├── .gitignore
├── .dockerignore
├── Dockerfile
└── pom.xml
```

## ⚙️ Configuration

### Environment Variables

The framework uses `.env` files for configuration. Priority order:

1. System properties (`-DBASE_URL=...`)
2. `.env` file
3. Default fallback values

**Available Variables:**

- `BASE_URL` - Base URL for the application under test (default: `https://www.automationexercise.com`)

### ConfigManager Usage

```java
// Get BASE_URL with fallback
String baseUrl = ConfigManager.getBaseUrl();

// Get any custom property
String value = ConfigManager.get("PROPERTY_NAME");

// Get with custom default
String value = ConfigManager.get("PROPERTY_NAME", "default-value");
```

## 📊 Test Reports

### Allure Reports

Generate and view Allure reports:

```bash
# Run tests
mvn clean test

# Generate report
mvn allure:report

# Open report in browser
mvn allure:serve
```

## 🐳 Docker

The Dockerfile is optimized for:

- **Layer caching**: Dependencies are cached separately from source code
- **Browser installation**: Playwright browsers are pre-installed
- **Environment variables**: Support for runtime configuration
- **Multi-stage builds**: Efficient image size

### Docker Features

- Base image: `maven:3.9.6-eclipse-temurin-17`
- Playwright browsers: Pre-installed Chromium
- Environment variables: Configurable at runtime
- Volume support: Test results can be mounted

Example with volume mounting:
```bash
docker run --rm -v $(pwd)/target:/app/target playwright-tests
```

## 🧰 Dependencies

Key dependencies in this project:

- **Playwright**: `1.46.0` - Browser automation
- **TestNG**: `7.9.0` - Test framework
- **REST Assured**: `5.4.0` - API testing
- **Allure**: `2.24.0` - Test reporting
- **Jackson**: `2.17.1` - JSON processing
- **JavaFaker**: `1.0.2` - Test data generation
- **dotenv-java**: `3.0.0` - Environment configuration

## 🤝 Contributing

1. Create your feature branch
2. Commit your changes
3. Push to the branch
4. Create a Pull Request

## 📝 Notes

- The `.env` file is excluded from version control for security
- Use `.env.example` as a template for team members
- System properties override `.env` values for flexibility
- Docker uses `.env.example` as default configuration

## 🔒 Security

⚠️ **Important**: Never commit `.env` files to version control. They may contain sensitive information.

- `.env` is in `.gitignore`
- `.env.example` provides a safe template
- Use environment variables for CI/CD secrets

## 📞 Support

For issues or questions, please open an issue in the repository.

