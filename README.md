# Chris Meilisearch Startup Validator

## Latest version
[![](https://jitpack.io/v/range79/Chris-meili-validator.svg)](https://jitpack.io/#range79/Chris-meili-validator)

This library solves a simple but annoying problem:

Spring Boot starts.  
Meilisearch is not fully ready yet.  
Your application crashes because it tries to use Meili too early.

This validator blocks application startup until Meilisearch is actually healthy.

---

## Modules

This project is split into two modules:

### core
- Pure Java
- No Spring dependency
- Can be used in any JVM application
- Manually triggered validation

### spring
- Spring Boot integration
- Automatically runs validation during application startup
- Depends on `core` (you don’t need to add `core` manually)

---

## Installation

### Maven

Add JitPack repository:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
````

#### Core only

```xml
<dependency>
  <groupId>com.github.range79</groupId>
  <artifactId>Chris-meili-validator-core</artifactId>
  <version>VERSION</version>
</dependency>
```

#### Spring Boot integration

```xml
<dependency>
  <groupId>com.github.range79</groupId>
  <artifactId>Chris-meili-validator-spring</artifactId>
  <version>VERSION</version>
</dependency>
```

---

### Gradle

Add JitPack repository:

```gradle
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

#### Core only

```gradle
implementation("com.github.range79:Chris-meili-validator-core:VERSION")
```

#### Spring Boot integration

```gradle
implementation("com.github.range79:Chris-meili-validator-spring:VERSION")
```

When you use the Spring module, `core` is included automatically.

---

## Configuration (Spring Boot)

Set Meili search URL in your application configuration:

```properties
meili.startup.url=<Your-Meili-app-url> like(http://localhost:7070)(default url is https://localhost:7070)
```

Optional settings:

```properties
# How often (in seconds) the system checks whether Meilisearch is ready.
# Default: 1
# Increasing this value is recommended when log-mode is enabled,
# because very small intervals may cause excessive log output.
meili.startup.interval=

# Maximum time (in seconds) to wait for Meili search to become ready.
# Default: 30
meili.startup.timeout=

# Optional API key.
# Only set this if your Meilisearch instance was started with an API key.
meili.startup.api-key=YOUR_MEILI_API_KEY

# default log mode is "info"
meili.startup.log-mode=



```
### What is `log-mode`?

The `log-mode` property controls how much information is logged during the Meilisearch startup validation process.

It determines the level of detail printed while the system checks whether Meilisearch is ready.

Available log modes:

- `NONE`  – Disables all startup logs.
- `INFO`  – Logs basic startup status and readiness information (default).
- `WARN`  – Logs only warning messages and unusual situations.
- `ERROR` – Logs only critical errors and failures.
- `DEBUG` – Logs detailed startup process information for troubleshooting.

Using `DEBUG` mode is recommended only during development or debugging, as it may generate excessive log output.


# what is interval ?
interval is the frequency at which requests are sent during the timeout period.
In other words, it defines how often the system should check Meili until the timeout is reached.



Tabii! İşte Markdown formatında, README’ye direkt ekleyebileceğin şekilde **uyarı/disclaimer** bölümü:


## ⚠Disclaimer: Master API Key Required

**Important:** If your Meilisearch instance requires a master API key:

- **Invalid API key**, or  
- **No API key provided** while the instance enforces authentication  

will cause the application to **terminate immediately**.

The validator will log a clear error message and then call:

```java
System.exit(1);
```

This ensures the application does **not continue running without proper authentication**, preventing any unsafe attempts to query Meilisearch.

