plugins {
    `java-library`
}

group = "com.range"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    compileOnly("org.springframework:spring-context:7.0.4")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:4.0.3")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor:4.0.3")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:4.0.3")


    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    testImplementation("org.junit.platform:junit-platform-engine:6.0.3")
    testImplementation("org.junit.platform:junit-platform-launcher:6.0.3")

    // Source: https://mvnrepository.com/artifact/org.testcontainers/testcontainers
    testImplementation("org.testcontainers:testcontainers:2.0.3")
    testImplementation("org.testcontainers:junit-jupiter:1.21.4")
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")

    // Spring Boot test starter
    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.3")
}

tasks.test {
    useJUnitPlatform()
    // opsiyonel debug/log
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}
