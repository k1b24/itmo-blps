import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("kapt")
}

version = "0.0.1-SNAPSHOT"

apply(plugin = "io.spring.dependency-management")

group = "ru.itmo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.zxing:core:3.3.0")
    implementation("com.google.zxing:javase:3.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
springBoot {
    buildInfo()
}

tasks.withType<BootJar> {
    archiveBaseName.set("qr-validator-service")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

kapt {
    useBuildCache = true
}
