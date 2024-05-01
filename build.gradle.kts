plugins {
    id("java")
}

group = "ru.cyn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("com.codeborne:selenide:7.0.4")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.codeborne:pdf-test:1.8.1")
    testImplementation("com.codeborne:xls-test:1.4.3")
    testImplementation("com.google.code.gson:gson:2.10.1")
    testImplementation("com.opencsv:opencsv:5.7.1")
    testImplementation("com.fasterxml.jackson.core:jackson-core:2.14.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
}

tasks.test {
    useJUnitPlatform()
}