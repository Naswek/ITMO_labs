plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "itmo.programming"
version = "1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("com.fasterxml.jackson:jackson-bom:2.15.0"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")

    implementation("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "main.Server",
                "Class-Path" to configurations.runtimeClasspath.get().files.joinToString(" ") { it.name }
            )
        )
    }
}

application {
    mainClass.set("itmo.programming.Server")
    applicationDistribution.into("lib") {
        from(tasks.shadowJar)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.isDebug = false
    options.compilerArgs.addAll(listOf("-g:none", "-Xlint:all", "-Werror"))
}




sourceSets {
    main {
        java {
            srcDirs("java")
        }
    }
}