plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "itmo.programming"
version = "1.2"

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

    implementation(files("libs/fastcgi-lib.jar"))

    implementation("com.google.code.gson:gson:2.11.0")

    //implementation("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.example.Server"
        )
    }

    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })
    archiveFileName = "lab1.jar"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}



tasks.withType<JavaCompile>().configureEach {
    options.isDebug = false
    options.compilerArgs.addAll(listOf("-g:none", "-Xlint:all", "-Werror"))
}

application {
    mainClass.set("org.example.Server")
}


sourceSets {
    main {
        java {
            srcDirs("java")
        }
    }
}