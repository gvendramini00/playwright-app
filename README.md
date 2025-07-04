# MyPlayRightApp – Automated UI Testing with Playwright + Spring Boot

This application integrates **Playwright** with **Spring Boot** to provide a RESTful interface for running automated UI tests against a website.

---

## 🚀 Getting Started

### 1. Clone the Project

```bash
git clone https://github.com/<your-org>/my-playwright-app.git
cd my-playwright-app
```

### 2. Install Playwright Browsers

You must install the required browsers for Playwright:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args=install
```

This will download Chromium, Firefox, and WebKit binaries needed for the tests.

---

## ▶️ Running the App

Compile and run the Spring Boot application:

```java
package org.gig.myplayrightapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyPlayRightAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyPlayRightAppApplication.class, args);
    }
}
```

Or simply run from terminal/IDE:

```bash
./mvnw spring-boot:run
```

---

## 🌐 Accessing the Test Endpoints

Once the app is running, open your browser and go to:

```
http://localhost:8080/api/test/testCaseXXX
```

Replace `testCaseXXX` with the test case you want to run:

- `testCase001` – Veridas registration
- `testCase002` – Veridas identity capture
- `testCase003` – Camera permission flow
- `testCase004` – Manual registration
- `testCase005` – Registration with missing data (validation check)
- (add more as needed)

---

## 📂 Screenshots

Each test saves a screenshot of the final state in the `/screenshots` folder for review and debugging.

---

## 🧪 API Documentation with Swagger

You can explore and trigger the available test cases via **Swagger UI**:

👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
Or, if that doesn't load:  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 💡 Notes

- Ensure you have a stable internet connection and that no modal dialogs block automated browser flows.
- You can customize timeouts and test steps inside each test case method in the service class.


## 🛠️ Dependencies

- Java 17+
- Maven
- Spring Boot 3.x
- Playwright for Java

---

## 📞 Support

If you encounter any issues or need help, feel free to open an issue or contact the maintainers.

---

© 2025 GIG Quality Team