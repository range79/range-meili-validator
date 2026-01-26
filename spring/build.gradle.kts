plugins {
    `java-library`
}

group = "com.range"


repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework:spring-context:7.0.3")
    implementation("org.springframework.boot:spring-boot-autoconfigure:4.0.0")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor:4.0.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:4.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
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