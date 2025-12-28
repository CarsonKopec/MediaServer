plugins {
    id("java-library")
}

group = "dev.imagineforgee.ms.shared.protocol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.moshi:moshi:1.15.0")
    api("com.squareup.moshi:moshi-kotlin:1.15.0")
    api("com.squareup.moshi:moshi-adapters:1.15.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}