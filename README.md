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
meili.startup.interval=    # default 1
meili.startup.timeout=    # second format default is 30

# API key is optional: only fill if your Meili instance was started with a key
meili.startup.api-key=YOUR_MEILI_API_KEY
```

# what is interval ?
interval is the frequency at which requests are sent during the timeout period.
In other words, it defines how often the system should check Meili until the timeout is reached.

