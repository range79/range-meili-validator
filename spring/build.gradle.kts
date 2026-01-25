plugins {
    `java-library`
}

group = "com.range"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:7.0.3")
    implementation(project(":core"))
    implementation("org.springframework:spring-beans:7.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
