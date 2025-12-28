plugins {
    id("java")
}

group = "dev.imagineforgee.ms"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Shared:protocol"))

    implementation("io.projectreactor.netty:reactor-netty-http:1.1.15")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
}

tasks.test {
    useJUnitPlatform()
}