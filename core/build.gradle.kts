import org.gradle.api.internal.provider.ValueSupplier.ValueProducer.task
import org.gradle.kotlin.dsl.task

plugins {
    `java-library`
}

group = "com.range"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    // Source: https://mvnrepository.com/artifact/com.squareup.okhttp3/mockwebserver
    testImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")


    testImplementation("org.junit.platform:junit-platform-engine:6.0.3")
    testImplementation("org.junit.platform:junit-platform-launcher:6.0.3")
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

