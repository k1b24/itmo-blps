import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("kapt")
}

version = "0.0.1-SNAPSHOT"

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.netty:netty-resolver-dns-native-macos::osx-aarch_64")
    implementation(project(mapOf("path" to ":sper-bank-model")))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

}

springBoot {
    buildInfo()
}

tasks.withType<BootJar> {
    archiveBaseName.set("sper-bank")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

kapt {
    useBuildCache = true
}