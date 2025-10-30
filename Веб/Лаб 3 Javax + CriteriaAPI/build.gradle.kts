plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("war")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

group = "itmo.programming"
version = "1.2"


tasks.withType<JavaCompile>().configureEach {
    options.release.set(11)
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
    implementation("org.primefaces:primefaces:8.0")

//    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")
//    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
//    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
//    implementation("jakarta.json.bind:jakarta.json.bind-api:3.0.1") -- джакарта(

    compileOnly("javax:javaee-web-api:8.0.1")
//    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")

    implementation("org.hibernate:hibernate-core:5.6.15.Final")

    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:5.1.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.isDebug = false
    options.compilerArgs.remove("-Werror") // без этого не компилируется этот javax, is deprecated!!
    options.compilerArgs.addAll(listOf("-g:none", "-Xlint:all"))
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

tasks.war {
    archiveFileName.set("ROOT.war")
    from("src/main/webapp")
}


