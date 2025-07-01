plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "itmo.programming"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("com.fasterxml.jackson:jackson-bom:2.15.0"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
}

sourceSets {
    main {
        java {
            srcDirs("java")
        }
    }
}