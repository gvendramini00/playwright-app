# MyPlayRightApp — Eclipse Setup Guide

> For project overview, endpoints, and environment variables see [README.md](README.md).

---

## Requirements

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.6+ |
| Eclipse | 2022-06 or newer |
| Spring Tools 4 | Recommended (see step 1) |

---

## 1. Install Spring Tools 4 (Recommended)

Spring Tools 4 adds Spring Boot run configuration support directly in Eclipse.

1. Go to **Help → Eclipse Marketplace**
2. Search for **Spring Tools 4**
3. Click **Install** and follow the prompts
4. Restart Eclipse when prompted

> Alternatively, download the pre-bundled [Spring Tools 4 for Eclipse](https://spring.io/tools) distro which includes everything out of the box.

---

## 2. Install the Lombok Agent

Eclipse requires an extra step to support Lombok annotations (`@Slf4j`, `@RequiredArgsConstructor`, etc.).

1. Download `lombok.jar` from [projectlombok.org](https://projectlombok.org/download)
2. Run the installer:
   ```bash
   java -jar lombok.jar
   ```
3. The installer detects your Eclipse installation — click **Install / Update**
4. Restart Eclipse

> Without this, Lombok annotations show as compile errors even though the project builds correctly via Maven.

---

## 3. Clone and Import the Project

### Option A — Clone from Eclipse

1. Go to **File → Import → Git → Projects from Git**
2. Select **Clone URI** and paste the repository URL
3. Follow the wizard; on the last step select **Import as general project**
4. After cloning, right-click the project → **Configure → Convert to Maven Project**

### Option B — Clone from terminal, then import

```bash
git clone https://github.com/<your-org>/playwright-app.git
```

Then in Eclipse:

1. Go to **File → Import → Maven → Existing Maven Projects**
2. Set the **Root Directory** to the cloned folder
3. Check `pom.xml` and click **Finish**

Eclipse downloads all dependencies automatically.

---

## 4. Set the Java Version

1. Right-click the project → **Properties → Java Compiler**
2. Uncheck **Use compliance from execution environment**
3. Set **Compiler compliance level** to **17** → **Apply and Close**

Verify the JRE:

1. Right-click the project → **Properties → Java Build Path → Libraries**
2. Confirm the JRE System Library is Java 17 or higher
3. If not, click **Edit** and select the correct JRE

---

## 5. Configure Environment Variables

Credentials are never stored in any committed file. Set them in the run configuration.

1. Copy `.env.example` to `.env` in the project root and fill in your credentials
2. Right-click `MyPlayRightAppApplication.java` → **Run As → Run Configurations**
3. Select the existing **Spring Boot App** entry (or create one under **Spring Boot App**)
   - If Spring Tools is not installed, use **Java Application** instead
4. Go to the **Environment** tab
5. Click **New** for each variable and fill in your values:

| Name | Value |
|------|-------|
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `ALIRA_USERNAME` | your Alira Pre username |
| `ALIRA_PASSWORD` | your Alira Pre password |
| `ALIRA_STAGING_USERNAME` | your Alira Staging username |
| `ALIRA_STAGING_PASSWORD` | your Alira Staging password |
| `STAGING_DB_USERNAME` | your Staging DB username |
| `STAGING_DB_PASSWORD` | your Staging DB password |
| `CGM_DB_USERNAME` | your CGM DB username |
| `CGM_DB_PASSWORD` | your CGM DB password |
| `GPPT_DB_USERNAME` | your GP-PT DB username |
| `GPPT_DB_PASSWORD` | your GP-PT DB password |

6. Click **Apply → Run**

Eclipse stores these values in your local workspace only — they are never committed to Git.

> See `.env.example` for the full list of variables including optional URL overrides.

> Staging DB credentials (`STAGING_DB_USERNAME`, `STAGING_DB_PASSWORD`) are only required when a staging endpoint is first called — the app starts without them.

---

## 6. Install Playwright Browsers

Open a terminal in the project root (**Window → Show View → Terminal** in Eclipse) and run once after cloning:

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

Right-click the project → **Run As → Maven build...**

In the **Goals** field enter:

```
clean package -DskipTests
```

Click **Run**. Or from a terminal:

```bash
mvn clean package -DskipTests
```

---

## 8. Run the Application

Right-click `MyPlayRightAppApplication.java` → **Run As → Spring Boot App**

(If Spring Tools is not installed: **Run As → Java Application**)

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

Set `SPRING_PROFILES_ACTIVE=dev` in the run configuration to open a visible browser window during test runs.

---

## Recording New Tests

Open a terminal in the project root and run:

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
A required environment variable is missing. Open **Run Configurations → Environment** tab and verify all variables from `.env.example` are present.

**Lombok annotations show as errors (`@Slf4j`, `@RequiredArgsConstructor`, etc.)**
Run the Lombok installer as described in step 2. The project compiles correctly via Maven regardless — Eclipse just needs the agent to resolve the annotations in the editor.

**"Spring Boot App" option not available in Run As**
Spring Tools 4 is not installed. Use **Java Application** instead — both work identically for running the app.

**Maven dependencies not downloading**
Right-click the project → **Maven → Update Project** → check **Force Update of Snapshots/Releases** → **OK**.

**Browser does not open during tests**
Make sure `SPRING_PROFILES_ACTIVE=dev` is set in the run configuration. Without it the app defaults to headless mode.

**Playwright browser not found on first run**
Run the install command from step 6 before triggering any test endpoint.

---

© 2026 GIG Quality Team
