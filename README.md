# 🛒 E-Commerce Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red)
![Maven](https://img.shields.io/badge/Maven-3.8.7-blue)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Tests](https://img.shields.io/badge/Tests-12%20Passing-brightgreen)

A professional, production-ready Selenium Java test automation framework
built for [AutomationExercise.com](https://automationexercise.com).
Follows industry best practices including Page Object Model, explicit waits,
ThreadLocal WebDriver management, and CI/CD integration.

---

## 📋 Table of Contents

- [Framework Architecture](#framework-architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Test Coverage](#test-coverage)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Running Tests](#running-tests)
- [Reports and Logs](#reports-and-logs)
- [CI/CD Pipeline](#cicd-pipeline)
- [Key Design Decisions](#key-design-decisions)
- [Author](#author)

---

## 🏗️ Framework Architecture

┌─────────────────────────────────────────────────────┐
│                    Test Layer                        │
│  LoginTest │ RegistrationTest │ CartTest │ etc.     │
├─────────────────────────────────────────────────────┤
│                   Base Layer                         │
│         BaseTest (lifecycle management)              │
├───────────────────┬─────────────────────────────────┤
│    Page Layer     │         Utility Layer            │
│  LoginPage        │  ConfigReader  │ AdHandler       │
│  RegistrationPage │  DriverFactory │ LoggerUtil      │
│  ProductSearchPage│  ScreenshotUtil│ ExtentReports   │
│  CartPage         │                                  │
│  CheckoutPage     │                                  │
├───────────────────┴─────────────────────────────────┤
│                  Config Layer                        │
│    config.properties │ log4j2.xml │ testng.xml       │
└─────────────────────────────────────────────────────┘

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Core programming language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test framework and assertions |
| Maven | 3.8.7 | Build tool and dependency management |
| WebDriverManager | 5.7.0 | Automatic ChromeDriver management |
| Extent Reports | 5.1.1 | HTML test reporting |
| Log4j2 | 2.23.0 | Logging framework |
| Apache Commons IO | 2.15.1 | File operations for screenshots |
| GitHub Actions | - | CI/CD pipeline |

---

## 📁 Project Structure

ecommerce-automation/
├── src/
│   ├── main/java/com/ecommerce/
│   │   ├── base/
│   │   │   ├── BaseTest.java           # TestNG lifecycle management
│   │   │   └── DriverFactory.java      # ThreadLocal WebDriver factory
│   │   ├── pages/
│   │   │   ├── LoginPage.java          # Login page interactions
│   │   │   ├── RegistrationPage.java   # Registration flow
│   │   │   ├── ProductSearchPage.java  # Product search operations
│   │   │   ├── CartPage.java           # Cart management
│   │   │   └── CheckoutPage.java       # Checkout and payment
│   │   └── utilities/
│   │       ├── AdHandler.java          # Google Ad overlay handler
│   │       ├── ConfigReader.java       # Properties file reader
│   │       ├── ExtentReportManager.java# HTML report generation
│   │       ├── LoggerUtil.java         # Centralized logger factory
│   │       └── ScreenshotUtil.java     # Failure screenshot capture
│   └── test/
│       ├── java/com/ecommerce/tests/
│       │   ├── LoginTest.java          # TC001-TC003
│       │   ├── RegistrationTest.java   # TC004-TC005
│       │   ├── ProductSearchTest.java  # TC006-TC007
│       │   ├── CartTest.java           # TC008-TC010
│       │   └── CheckoutTest.java       # TC011-TC012
│       └── resources/
│           ├── config.properties       # Environment configuration
│           └── log4j2.xml             # Logging configuration
├── screenshots/                        # Auto-captured failure screenshots
├── reports/                           # Extent HTML reports
├── logs/                              # Log4j log files
├── testng.xml                         # TestNG suite configuration
└── pom.xml                            # Maven dependencies

---

## ✅ Test Coverage

| Phase | Test Case | Description | Status |
|-------|-----------|-------------|--------|
| Phase 1 | TC001 | Valid Login | ✅ Pass |
| Phase 1 | TC002 | Invalid Login - Wrong Password | ✅ Pass |
| Phase 1 | TC003 | Empty Credentials | ✅ Pass |
| Phase 2 | TC004 | Successful New User Registration | ✅ Pass |
| Phase 2 | TC005 | Registration with Existing Email | ✅ Pass |
| Phase 3 | TC006 | Search Existing Product | ✅ Pass |
| Phase 3 | TC007 | Search Non-Existing Product | ✅ Pass |
| Phase 4 | TC008 | Add Single Product to Cart | ✅ Pass |
| Phase 4 | TC009 | Add Multiple Products to Cart | ✅ Pass |
| Phase 4 | TC010 | Verify Cart Quantity | ✅ Pass |
| Phase 5 | TC011 | Proceed to Checkout | ✅ Pass |
| Phase 5 | TC012 | Complete Order with Payment | ✅ Pass |

**Total: 12 Tests | 0 Failures**

---

## ⚙️ Prerequisites

Ensure the following are installed:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17+ | [adoptium.net](https://adoptium.net) |
| Maven | 3.8+ | [maven.apache.org](https://maven.apache.org) |
| Google Chrome | Latest | [google.com/chrome](https://google.com/chrome) |
| Git | Any | [git-scm.com](https://git-scm.com) |

Verify installations:
```bash
java -version
mvn -version
git --version
```

---

## 🚀 Setup and Installation

**1. Clone the repository:**
```bash
git clone https://github.com/YOUR_USERNAME/ecommerce-automation.git
cd ecommerce-automation
```

**2. Configure test credentials:**

Open `src/test/resources/config.properties` and update:
```properties
valid.email=your_registered_email@example.com
valid.password=your_password
register.existing.email=your_registered_email@example.com
```

> ⚠️ Register an account at [automationexercise.com](https://automationexercise.com)
> before running tests.

**3. Install dependencies:**
```bash
mvn dependency:resolve
```

---

## ▶️ Running Tests

**Run all tests:**
```bash
mvn test
```

**Run a specific test class:**
```bash
mvn test -Dtest=LoginTest
```

**Run in headless mode (no browser window):**

Set in `config.properties`:
```properties
headless=true
```

**Run specific phase only:**

Edit `testng.xml` to include only the desired test section,
then run `mvn test`.

---

## 📊 Reports and Logs

**Extent HTML Report:**
```bash
# Generated after each run at:
reports/TestReport_YYYY-MM-DD_HH-mm-ss.html

# Open in browser:
xdg-open reports/TestReport_*.html      # Linux
open reports/TestReport_*.html           # Mac
start reports/TestReport_*.html          # Windows
```

**Log File:**
```bash
# Available at:
logs/automation.log

# View latest logs:
tail -f logs/automation.log
```

**Failure Screenshots:**
```bash
# Auto-captured on test failure at:
screenshots/testMethodName_YYYY-MM-DD_HH-mm-ss.png
```

---

## 🔄 CI/CD Pipeline

This project uses **GitHub Actions** for continuous integration.

The pipeline automatically:
- Triggers on every push to `main` branch
- Triggers on every Pull Request
- Sets up Java 17 environment
- Runs all 12 tests in headless mode
- Uploads test reports as artifacts

See [`.github/workflows/ci.yml`](.github/workflows/ci.yml) for configuration.

---

## 💡 Key Design Decisions

**1. ThreadLocal WebDriver**
Each test thread gets its own WebDriver instance, enabling safe parallel execution without test interference.

**2. Page Object Model**
All element locators and interactions are encapsulated in page classes. Tests never interact with elements directly, making the framework resilient to UI changes.

**3. Explicit Waits over Implicit**
Every element interaction uses `WebDriverWait` with specific `ExpectedConditions`, eliminating race conditions and `Thread.sleep()` usage.

**4. Externalized Configuration**
All environment-specific values (URL, credentials, timeouts) live in `config.properties`. Switching environments requires changing one file, not touching test code.

**5. JavaScript Executor for Ad Handling**
The target site serves Google Ads that intercept element clicks. `AdHandler` removes ad iframes via JavaScript before interactions, making tests stable in real-world conditions.

**6. Automatic Screenshot Capture**
`ITestResult` in `@AfterMethod` detects test failures and triggers `ScreenshotUtil` to capture the browser state at the exact moment of failure.

---

## 👨‍💻 Author

**Janitha**
- GitHub: [@JAniii99](https://github.com/JAniii99)
- LinkedIn: [janithasandaruwan](https://www.linkedin.com/in/janithasandaruwan)
- Portfolio: [janithaqa.me](https://janithaqa.me)

---

## 📄 License

This project is open source and available under the
[MIT License](LICENSE).