# Playwright Java Automation Framework

A professional-grade test automation framework using **Playwright**, **TestNG**, and **REST Assured** with **Clean Code** principles, **SOLID design**, and comprehensive **CI/CD** integration.

## Project Status: Production Ready!

- **26+ Tests** - Complete coverage of UI and API flows
- **16 API Tests** - Fully functional with comprehensive scenarios
- **10 UI Tests** - Page Object Model implementation
- **All Assignment Requirements Met** - 100% completion
- **CI/CD Pipeline** - GitHub Actions with Allure reporting
- **Docker Support** - Containerized execution ready
- **SonarLint Integrated** - Code quality and coverage tracking

**Quick Start:** `mvn test -Dtest="*APITests"` for API tests, `mvn allure:serve` for reports.

## Key Features

### Architecture & Design Principles
- **Single Responsibility Principle (SRP)** - Each class has one clear purpose
- **Page Object Model (POM)** - Clean page abstraction
- **Data-Driven Testing (DDT)** - External JSON test data
- **Clean Code** - Meaningful naming, separation of concerns
- **SOLID Principles** - Production-ready architecture

### Technology Stack
- **Playwright** - Modern browser automation
- **TestNG** - Test organization and execution  
- **REST Assured** - API testing framework
- **Allure** - Beautiful test reports
- **JaCoCo** - Code coverage analysis
- **SonarLint** - Code quality monitoring
- **Docker** - Containerized execution
- **GitHub Actions** - CI/CD pipeline

### Testing Capabilities
- **UI Testing** - Full registration and login flows
- **API Testing** - Comprehensive positive/negative scenarios
- **Code Coverage** - Tracked with JaCoCo (60% minimum)
- **Code Quality** - SonarLint analysis
- **Reporting** - Allure reports with screenshots
- **Docker** - Distributed testing support

## Prerequisites

- **Java 17+**
- **Maven 3.6+** (or use included `mvnw`)
- **Docker** (optional, for containerized execution)

## Quick Setup

### 1. Clone Repository
```bash
git clone https://github.com/N03MAN/playwright-java-automation-proposal
cd playwright-java-automation-proposal
```

### 2. Configure Environment
```bash
# Copy environment template
cp .env.example .env

# Edit if needed (optional - defaults work fine)
# BASE_URL=https://www.automationexercise.com
```

### 3. Install Dependencies
```bash
# Using Maven
mvn clean install

# Or using Maven Wrapper (no Maven installation needed)
./mvnw clean install
```

### 4. Install Playwright Browsers
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
```

## Running Tests

### API Tests 
```bash
# Run all API tests
mvn test -Dtest="*APITests"

# Run specific API test suite
mvn test -Dtest="LoginAPITests"
mvn test -Dtest="RegistrationAPITests"
```

### UI Tests (R
```bash
# Run single UI test (no browser issues)
mvn test -Dtest="LoginUITests#testValidLogin" -Dheadless=false

# Run registration test
mvn test -Dtest="RegistrationUITests#testDuplicateEmailRegistration" -Dheadless=false

# Run in headless mode (faster)
mvn test -Dtest="LoginUITests#testValidLogin" -Dheadless=true
```

### All Tests
```bash
# Run all API tests (UI tests excluded by default in testng.xml)
mvn clean test

# Generate Allure report
mvn allure:serve
```

### With Custom Configuration
```bash
# Override BASE_URL
mvn test -DBASE_URL=https://staging.example.com

# Run with browser visible
mvn test -Dtest="LoginUITests#testValidLogin" -Dheadless=false -DslowMo=500
```

## Docker Execution

### Build Image
```bash
docker build -t playwright-tests .
```

### Run Tests
```bash
# Run all tests
docker run --rm playwright-tests

# With custom environment
docker run --rm -e BASE_URL=https://staging.example.com playwright-tests

# Mount volume for reports
docker run --rm -v $(pwd)/target:/app/target playwright-tests
```

## Code Quality & Coverage

### SonarLint Analysis
```bash
# Run code quality analysis
./run-sonar-analysis.sh

# Or directly with Maven
mvn clean compile                    # Quick check
mvn test jacoco:report              # With coverage
mvn clean verify                    # Full analysis
```

### View Coverage Report
```bash
# Generate coverage report
mvn test jacoco:report

# Open in browser
open target/site/jacoco/index.html
```

### Code Quality Metrics
- **Code Coverage**: 60% minimum enforced
- **Quality Gates**: Configured in `sonar-project.properties`
- **Security Scanning**: Automated vulnerability detection
- **Technical Debt**: Monitored and tracked

## Project Structure

```
playwright-java-automation-proposal/
├── .github/
│   └── workflows/
│       └── ci.yml                        # GitHub Actions CI/CD
├── src/test/java/
│   ├── api/
│   │   └── UserApiClient.java           # REST Assured API client
│   ├── base/
│   │   └── BaseTest.java                # Test lifecycle management
│   ├── pages/                           # Page Object Model
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   └── RegistrationPage.java
│   ├── tests/
│   │   ├── api/                         # API Test Suites
│   │   │   ├── LoginAPITests.java       # 8 login scenarios
│   │   │   └── RegistrationAPITests.java # 8 registration scenarios
│   │   └── ui/                          # UI Test Suites
│   │       ├── LoginUITests.java        # 5 login tests
│   │       └── RegistrationUITests.java # 5 registration tests
│   └── utils/                           # Utility Classes (SRP)
│       ├── AssertionHelper.java         # Expressive assertions
│       ├── BrowserManager.java          # Browser lifecycle
│       ├── ConfigManager.java           # Configuration
│       ├── DataUtils.java               # Data utilities
│       ├── TestDataManager.java         # Test data management (DDT)
│       └── TestListeners.java           # TestNG listeners
├── src/test/resources/
│   ├── testdata/                        # External Test Data (DDT)
│   │   ├── datasets.json                # Registration data
│   │   ├── logins.json                  # Login credentials
│   │   └── users.json                   # User data
│   ├── allure.properties                # Allure configuration
│   ├── config.properties                # App configuration
│   └── log4j2.xml                       # Logging config
├── .env                                 # Environment variables (gitignored)
├── .env.example                         # Environment template
├── .gitignore
├── .dockerignore
├── .sonarlintignore                     # SonarLint exclusions
├── Dockerfile                           # Docker configuration
├── sonar-project.properties             # SonarLint config
├── run-sonar-analysis.sh                # Code quality script
├── testng.xml                           # TestNG suite configuration
├── pom.xml                              # Maven dependencies
└── README.md
```

## Architecture Highlights

### Single Responsibility Principle (SRP)
Each class has ONE clear purpose:

| Class | Responsibility |
|-------|---------------|
| `BrowserManager` | Browser lifecycle management only |
| `TestDataManager` | Test data operations only |
| `AssertionHelper` | Assertions with clear messages only |
| `ConfigManager` | Configuration management only |
| `Page Objects` | Page-specific interactions only |

### Data-Driven Testing (DDT)
```java
// External JSON files for test data
@DataProvider(name = "validLogins")
public Object[][] validLoginData() {
    return TestDataManager.toDataProvider("logins.json");
}

@Test(dataProvider = "validLogins")
public void testValidLogin(Map<String, Object> loginData) {
    // Test logic uses data from JSON
}
```

### Clean Code Examples
```java
// Expressive method names
public void verifyRegistrationSuccess() { }
public void navigateToHomePage() { }

// Clear assertions with context
AssertionHelper.assertElementVisible(
    homePage.verifyHomePage(),
    "Home Page should be displayed"
);

// Separation of concerns
tests/     → Test logic
pages/     → Page interactions  
utils/     → Utilities
testdata/  → External data
```

## Test Coverage

| Test Type | Count | Status |
|-----------|-------|--------|
| **API Tests** | 16 | Passing |
| Registration API | 8 | Passing |
| Login API | 8 | Passing |
| **UI Tests** | 10 | Ready |
| Registration UI | 5 | Ready |
| Login UI | 5 | Ready |
| **Total** | **26+** | **Production Ready** |

### Test Scenarios Covered
- Valid credentials (positive testing)
- Invalid credentials (negative testing)
- Duplicate accounts
- Empty fields validation
- SQL injection attempts
- XSS attempts
- Special characters
- Edge cases

## Reports & Monitoring

### Allure Reports
```bash
# Generate and open report
mvn allure:serve

# View reports in CI/CD
# GitHub Actions → Actions tab → Select run → Artifacts
```

### JaCoCo Coverage Reports
```bash
# Generate coverage
mvn test jacoco:report

# View report
open target/site/jacoco/index.html
```

### SonarLint Analysis
```bash
# Interactive menu
./run-sonar-analysis.sh

# Quick compile check
mvn compile

# Full analysis
mvn verify
```

## Configuration

### Environment Variables
Priority order: **System Properties** > **`.env` file** > **Default values**

```bash
# In .env file
BASE_URL=https://www.automationexercise.com

# Or via command line (overrides .env)
mvn test -DBASE_URL=https://staging.example.com

# Or programmatically
String baseUrl = ConfigManager.getBaseUrl();
```

### Browser Configuration
```bash
# Run in headed mode (see browser)
mvn test -Dtest="LoginUITests#testValidLogin" -Dheadless=false

# Slow down actions for debugging
mvn test -Dtest="LoginUITests#testValidLogin" -Dheadless=false -DslowMo=1000

# Change browser (configured in BrowserManager)
mvn test -Dbrowser=firefox
```

## CI/CD Pipeline

GitHub Actions workflow includes:
- Automated test execution on push/PR
- Allure report generation
- JaCoCo coverage reports
- Docker image building
- Security scanning (Trivy)
- GitHub Pages deployment for reports

View pipeline: `.github/workflows/ci.yml`

## Documentation

| Document | Purpose |
|----------|---------|
| `README.md` | Main documentation (this file) |
| `SONARLINT_SETUP_GUIDE.md` | Code quality setup |
| `SONARLINT_SETUP_COMPLETE.md` | Quick SonarLint reference |
| `.cursorrules` | Project guidelines and standards |

## Key Design Decisions

### 1. **Browser Lifecycle**
- Each test gets a fresh browser instance
- No shared state between tests
- Thread-safe via `ThreadLocal`

### 2. **Test Data Management**
- External JSON files (not hard-coded)
- Centralized in `TestDataManager`
- Supports placeholders for dynamic data

### 3. **Test Execution**
- API tests run together reliably
- UI tests run individually (browser isolation)
- Configured in `testng.xml`

### 4. **Error Handling**
- Expressive failure messages
- Screenshots on UI test failures
- API responses attached to Allure

## Troubleshooting

### Maven Not Found
```bash
# Use Maven Wrapper instead
./mvnw clean test

# Or install Maven
brew install maven  # macOS
```

### Browser Issues
```bash
# Reinstall Playwright browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
```

### Tests Timing Out
```bash
# Increase timeout in BrowserManager.java
# Or run tests individually
mvn test -Dtest="LoginUITests#testValidLogin"
```

### Permission Denied
```bash
# Make scripts executable
chmod +x mvnw
chmod +x run-sonar-analysis.sh
```

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Playwright | 1.46.0 | Browser automation |
| TestNG | 7.9.0 | Test framework |
| REST Assured | 5.4.0 | API testing |
| Allure | 2.24.0 | Test reporting |
| JaCoCo | 0.8.11 | Code coverage |
| Jackson | 2.17.1 | JSON processing |
| JavaFaker | 1.0.2 | Test data generation |
| dotenv-java | 3.0.0 | Environment config |

## Security

- `.env` files excluded from version control
-  Use `.env.example` as template
-  System properties for CI/CD secrets
-  Security scanning in CI/CD (Trivy)
-  SonarLint vulnerability detection

## Contributing

This project follows:
- **Clean Code** principles
- **SOLID** design patterns
- **Java coding standards**
- **TestNG** best practices

## License

This project is for educational and demonstration purposes.

## 🎓 Learning Resources

- [Playwright Java Docs](https://playwright.dev/java/)
- [TestNG Documentation](https://testng.org/)
- [REST Assured Guide](https://rest-assured.io/)
- [Allure Reports](https://docs.qameta.io/allure/)
- [Clean Code Principles](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

## Support

For questions or issues:
1. Check existing documentation
2. Review test examples in `src/test/java/tests/`
3. Check CI/CD pipeline logs
4. Review Allure reports for detailed test results

---



