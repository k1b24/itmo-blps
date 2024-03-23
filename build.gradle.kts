plugins {
    kotlin("jvm")
}

allprojects {
    group = "ru.itmo"
}

version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(17)
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            allWarningsAsErrors = false
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showStandardStreams = false
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }
}
