# MyPlayRightApp — IntelliJ IDEA Setup Guide

> For project overview, endpoints, and environment variables see [README.md](README.md).

---

## Requirements

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.6+ |
| IntelliJ IDEA | Community or Ultimate (2022.1+) |

---

## 1. Clone and Open the Project

1. Open IntelliJ IDEA
2. Go to **File → New → Project from Version Control**
3. Paste the repository URL and click **Clone**
4. IntelliJ detects the Maven project automatically — click **Load Maven Project** if prompted

---

## 2. Set the Java SDK

1. Go to **File → Project Structure → Project**
2. Set **SDK** to Java 17 or higher
3. Set **Language level** to 17
4. Click **Apply → OK**

---

## 3. Install the Lombok Plugin

1. Go to **File → Settings → Plugins**
2. Search for **Lombok** and install it
3. Restart IntelliJ when prompted
4. Go to **File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors**
5. Check **Enable annotation processing** → **Apply**

> Without this, `@Slf4j`, `@RequiredArgsConstructor`, and other Lombok annotations show as errors.

---

## 4. Configure Environment Variables

Credentials are never stored in any committed file. There are two ways to load them.

### Option A — EnvFile plugin (recommended)

The EnvFile plugin loads your `.env` file automatically on every run — no manual copy-pasting needed.

1. Go to **File → Settings → Plugins → Marketplace**
2. Search for **EnvFile** (by Borys Pierov) and install it
3. Restart IntelliJ
4. Copy `.env.example` to `.env` in the project root and fill in your credentials
5. Open **Run → Edit Configurations → MyPlayRightAppApplication**
6. Go to the **EnvFile** tab → check **Enable EnvFile** → click **+** → select your `.env` file
7. Click **OK**

The app now picks up all credentials from `.env` on every run.

### Option B — Environment variables field

1. Open **Run → Edit Configurations**
2. Select **MyPlayRightAppApplication** (or create one — see below)
3. Click the **`...`** icon next to **Environment variables**
4. Add each variable from `.env.example` with your own values
5. Click **OK**

---

## 5. Create a Run Configuration (if none exists)

### Ultimate Edition

1. Open **Run → Edit Configurations → +**
2. Choose **Spring Boot**
3. Set **Main class** to `org.gig.myplayrightapp.MyPlayRightAppApplication`
4. Apply environment variables as above

### Community Edition

Community does not include the Spring Boot config type. Use **Application** instead — it works identically.

1. Open **Run → Edit Configurations → +**
2. Choose **Application**
3. Set **Name** to `MyPlayRightAppApplication`
4. Set **Main class** to `org.gig.myplayrightapp.MyPlayRightAppApplication`
5. Set **Module** to `myplayrightapp`
6. Apply environment variables as above

> If the `...` icon is not visible next to Environment variables, click inside the field — the icon appears at the right edge of the text box.

---

## 6. Install Playwright Browsers

Open the **Terminal** tab at the bottom of IntelliJ and run once after cloning:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install --with-deps chromium"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium"
```

---

## 7. Build the Project

In the **Maven** panel (right side), double-click **Lifecycle → package**, or run from the terminal:

```bash
mvn clean package -DskipTests
```

---

## 8. Run the Application

Click the green **Run** button or press **Shift+F10**.

The app starts on `http://localhost:8080`. Confirm it is running:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Environment Profiles

| Profile | Headless | SQL logging | When to use |
|---------|----------|-------------|-------------|
| *(default)* | `true` | `true` | CI / headless |
| `dev` | `false` | `true` | Local — watch the browser |
| `prod` | `true` | `false` | Production |

Set `SPRING_PROFILES_ACTIVE=dev` in your `.env` or run configuration to open a visible browser window during test runs.

---

## Recording New Tests

Open the IntelliJ Terminal and run:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=codegen https://your-site-url.com"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://your-site-url.com"
```

A browser and a code panel open. Interact with the page and copy the generated Java selectors into your service implementation.

---

## Troubleshooting

**"Could not resolve placeholder" on startup**
A required environment variable is missing. Open **Run → Edit Configurations**, check the EnvFile tab or Environment variables field, and verify all variables from `.env.example` are present.

**"Spring Boot" option not available when creating a run config**
Expected in Community Edition. Choose **Application** instead and set the main class manually.

**Maven panel not visible**
Go to **View → Tool Windows → Maven**. If Maven is still not detected, right-click `pom.xml` → **Add as Maven Project**.

**Lombok annotations show as errors (`@Slf4j`, `@RequiredArgsConstructor`, etc.)**
Install the Lombok plugin (step 3) and enable annotation processing.

**Browser does not open during tests**
Make sure `SPRING_PROFILES_ACTIVE=dev` is set. Without it the app defaults to headless mode.

**Playwright browser not found on first run**
Run the install command from step 6 before triggering any test endpoint.

---

© 2026 GIG Quality Team
