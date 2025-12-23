

plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("war")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17);
    }
}

group = "itmo.programming"
version = "1.2"


tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {

    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.jupiter:junit-jupiter")

    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    implementation("com.google.code.gson:gson:2.11.0")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
    implementation("jakarta.json.bind:jakarta.json.bind-api:3.0.1")

    implementation("org.hibernate:hibernate-core:5.6.15.Final")

    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.mindrot:jbcrypt:0.4")

    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")

    // Jackson Provider for JAX-RS / Jakarta REST
    implementation("com.fasterxml.jackson.jakarta.rs:jackson-jakarta-rs-json-provider:2.17.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.isDebug = false
//    options.compilerArgs.remove("-Werror")
    options.compilerArgs.addAll(listOf("-g:none", "-Xlint:all"))
}

sourceSets {
    main {
        java {
            srcDirs("java")
        }
    }
}

tasks.jar {
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })
    archiveFileName = "lab1.jar"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}