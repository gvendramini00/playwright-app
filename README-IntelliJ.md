# MyPlayRightApp — Setup Guide for IntelliJ IDEA

A Spring Boot REST API that exposes Playwright browser automation tests as HTTP endpoints.
Trigger a test case by calling its URL; the app launches a browser, executes the flow, saves a screenshot, and returns a result string.

Supports three brands: **Alira** (back office), **Casino Gran Madrid (CGM)**, and **Golden Park Portugal (GP-PT)**.

---

## Requirements

| Tool    | Version |
|---------|---------|
| Java    | 17+     |
| Maven   | 3.6+    |
| MySQL   | Accessible dev/pre-prod instances for CGM and GP-PT |
| IntelliJ IDEA | Community or Ultimate |

---

## 1. Clone and Open the Project

1. Open IntelliJ IDEA
2. Go to **File → New → Project from Version Control**
3. Paste the repository URL and click **Clone**
4. IntelliJ will detect the Maven project automatically — click **Load Maven Project** if prompted

---

## 2. Set the Java SDK

1. Go to **File → Project Structure → Project**
2. Set **SDK** to Java 17 or higher
3. Set **Language level** to 17
4. Click **Apply → OK**

---

## 3. Configure Environment Variables

Credentials are never stored in any committed file. Set them in the run configuration.

The steps differ slightly between **Ultimate** and **Community** editions.

---

### IntelliJ IDEA Ultimate

Ultimate includes native Spring Boot run configuration support.

1. Open **Run → Edit Configurations**
2. If no configuration exists yet, click **+** and choose **Spring Boot**
3. Set **Main class** to `org.gig.myplayrightapp.MyPlayRightAppApplication`
4. Click the **`...`** icon next to **Environment variables**
5. Paste the following block, replacing each value with your own:

```
SPRING_PROFILES_ACTIVE=dev;ALIRA_USERNAME=your_alira_user;ALIRA_PASSWORD=your_alira_password;CGM_DB_USERNAME=your_db_user;CGM_DB_PASSWORD=your_db_password;GPPT_DB_USERNAME=your_db_user;GPPT_DB_PASSWORD=your_db_password
```

6. Click **OK**

---

### IntelliJ IDEA Community

Community does not include the Spring Boot run config type. Use a plain **Application** config instead — it works identically for running the app.

1. Open **Run → Edit Configurations**
2. Click **+** and choose **Application**
3. Set **Name** to `MyPlayRightAppApplication`
4. Set **Main class** to `org.gig.myplayrightapp.MyPlayRightAppApplication`
   - Click the folder icon or type the class name and press **Enter** to let IntelliJ resolve it
5. Make sure **Module** is set to `myplayrightapp` (or whichever module appears in the list)
6. Click the **`...`** icon next to **Environment variables**
7. Paste the following block, replacing each value with your own:

```
SPRING_PROFILES_ACTIVE=dev;ALIRA_USERNAME=your_alira_user;ALIRA_PASSWORD=your_alira_password;CGM_DB_USERNAME=your_db_user;CGM_DB_PASSWORD=your_db_password;GPPT_DB_USERNAME=your_db_user;GPPT_DB_PASSWORD=your_db_password
```

8. Click **OK**

> **Tip for Community users:** If you do not see the `...` icon next to Environment variables, click inside the field and look for the small icon at the right edge of the text box — it opens the same variable editor.

---

IntelliJ stores these values in your local run configuration only — they are never committed to Git.

> See `.env.example` at the project root for the full list of variables and their descriptions.

---

## 4. Install Playwright Browsers

Open the **Terminal** tab at the bottom of IntelliJ and run:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install --with-deps chromium"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium"
```

This downloads the Chromium binary used by all tests. Run once after cloning.

---

## 5. Build the Project

In the **Maven** side panel (right side), expand the project and double-click:

```
Lifecycle → package
```

Or run from the terminal:

```bash
mvn clean package -DskipTests
```

---

## 6. Run the Application

Click the green **Run** button next to `MyPlayRightAppApplication`, or press **Shift+F10**.

The app starts on `http://localhost:8080`.

To confirm it is running, open your browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Environment Profiles

| Profile       | Headless | SQL logging | When to use          |
|---------------|----------|-------------|----------------------|
| *(default)*   | `true`   | `true`      | CI / headless runs   |
| `dev`         | `false`  | `true`      | Local — watch browser|
| `prod`        | `true`   | `false`     | Production           |

The run configuration above sets `SPRING_PROFILES_ACTIVE=dev`, which opens a visible browser window when tests run. Remove that variable or change it to `prod` for fully headless execution.

---

## 7. Record a New Test

Use the Playwright code generator to record interactions on any page.

Open the IntelliJ **Terminal** and run:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=codegen https://your-site-url.com"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://your-site-url.com"
```

A browser and a code panel will open. Interact with the page and copy the generated Java selectors into your service implementation.

---

## Test Endpoints

Base URL: `http://localhost:8080/api/test/`

### Alira — Back Office

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/alira/testCase001` | Login to Alira Back Office |
| `GET /api/test/alira/testCase002` | Navigate to Player Profile |
| `GET /api/test/alira/navigation/games/testCase003` | Navigate → Games → Rooms |
| `GET /api/test/alira/navigation/games/testCase004` | Navigate → Games → Process Rooms |
| `GET /api/test/alira/navigation/games/testCase005` | Navigate → Games → Lobby Rooms |
| `GET /api/test/alira/navigation/games/testCase006` | Navigate → Games → Providers |
| `GET /api/test/alira/navigation/games/testCase007` | Navigate → Games → Themes & Tags |
| `GET /api/test/alira/navigation/games/testCase008` | Navigate → Games → Exchange Profile |
| `GET /api/test/alira/navigation/website/testCase009` | Navigate → Website → CMS |
| `GET /api/test/alira/navigation/website/testCase010` | Navigate → Website → Configuration → CMS Access |
| `GET /api/test/alira/navigation/website/testCase011` | Navigate → Website → Configuration → Constants |
| `GET /api/test/alira/navigation/marketing/testCase012` | Navigate → Dashboard — verify table has data |
| `GET /api/test/alira/navigation/marketing/testCase013` | Navigate → Marketing → Bonus → Deposit Promotions — verify table has data |
| `GET /api/test/alira/navigation/marketing/testCase014` | Navigate → Marketing → Bonus → Deposit Promotions → click New — verify modal appears |
| `GET /api/test/alira/navigation/marketing/testCase015` | Navigate → Marketing → Bonus → Deposit Promotions → create a new deposit promotion |
| `GET /api/test/alira/navigation/marketing/testCase016` | Navigate → Marketing → Bonus → Deposit Promotions → edit the first row and verify the update |
| `GET /api/test/alira/navigation/marketing/testCase017` | Navigate → Marketing → Bonus → Deposit Promotions → delete the first row and verify removal |

### Casino Gran Madrid (CGM)

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/cgm/testCase001` | Open pre-production site |
| `GET /api/test/cgm/testCase002` | Click register button, validate modal |
| `GET /api/test/cgm/testCase003` | Fast registration via Veridas (camera flow) |
| `GET /api/test/cgm/testCase004` | Manual registration with a generated unique player |
| `GET /api/test/cgm/testCase005` | Manual registration with missing name (validation check) |
| `GET /api/test/cgm/testCase006` | Manual registration with duplicate DNI/NIE (duplicate check) |

### Golden Park Portugal (GP-PT)

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/gp-pt/register` | Manual registration with a generated unique PT player |
| `GET /api/test/gp-pt/register-duplicate` | Registration with duplicate NIF (duplicate check) |

---

## Screenshots

Every test saves a screenshot of its final state under `/screenshots/`.
The folder is git-ignored. Screenshots are named after the test case (e.g., `testCase001.png`) or timestamped for runs that create unique data.

---

## Generated Test Data

Successful registration tests log the created user to `generated-users.csv` (email, national ID, alias, screenshot path). This file serves as an audit trail for test-created accounts.

---

## Troubleshooting (Community Edition)

**"Could not resolve placeholder" on startup**
The app could not find one of the required environment variables. Open **Run → Edit Configurations**, select your `MyPlayRightAppApplication` config, and verify all variables are present in the **Environment variables** field. A missing or misspelled variable name will cause this error.

**"Spring Boot" option not available when creating a run config**
This is expected in Community Edition. Choose **Application** instead and set the main class manually to `org.gig.myplayrightapp.MyPlayRightAppApplication`.

**Maven panel not visible**
Go to **View → Tool Windows → Maven**. If Maven is still not detected, right-click `pom.xml` → **Add as Maven Project**.

**Lombok annotations not recognized (red underlines on `@Slf4j`, `@RequiredArgsConstructor`, etc.)**
Install the Lombok plugin:
1. Go to **File → Settings → Plugins**
2. Search for **Lombok**
3. Install and restart IntelliJ
4. Go to **File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors**
5. Check **Enable annotation processing**

**App starts but browser does not open**
Make sure `SPRING_PROFILES_ACTIVE=dev` is set in the run configuration. Without it the app defaults to headless mode and no browser window appears.

**Playwright browser not found on first run**
Run the install command from the terminal (step 4) before triggering any test endpoint.

---

## Notes

- Tests use a real database connection to avoid generating duplicate players. Ensure the DB is reachable before running registration tests.
- `testCase003` (Veridas) requires camera permission — the browser is launched with `--use-fake-device-for-media-stream` so it works headless.
- Timeouts and selectors can be adjusted in the corresponding service implementation class.

---

## Dependencies

- Java 17+
- Spring Boot 3.5
- Playwright for Java 1.44
- Spring Data JPA + MySQL
- SpringDoc OpenAPI (Swagger)

---

© 2025 GIG Quality Team
