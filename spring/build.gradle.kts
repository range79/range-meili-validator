plugins {
    `java-library`
}

group = "com.range"


repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    compileOnly("org.springframework:spring-context:7.0.3")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:4.0.0")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor:4.0.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:4.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    // Source: https://mvnrepository.com/artifact/org.testcontainers/testcontainers
    testImplementation("org.testcontainers:testcontainers:2.0.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}