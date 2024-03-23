rootProject.name = "itmo-blps"
include("kachalka-backend", "sper-bank", "sper-bank-model", "qr-validator-service")

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
    }

    repositories {
        mavenCentral()
    }
}
include("sper-bank-model")
