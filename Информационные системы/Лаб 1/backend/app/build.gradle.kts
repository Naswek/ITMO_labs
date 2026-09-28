plugins {
    war
}

group = "org.example"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

val eclipselinkModule = configurations.create("eclipselinkModule")

dependencies {
    providedCompile("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    providedCompile("org.eclipse.persistence:eclipselink:4.0.8")
    add(eclipselinkModule.name, "org.eclipse.persistence:eclipselink:4.0.8")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.mindrot:jbcrypt:0.4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("labworks.war")
    from(file("../../frontend/dist"))
}

tasks.register<Copy>("copyPostgresDriver") {
    from(configurations.runtimeClasspath) {
        include("postgresql-*.jar")
        rename { "postgresql.jar" }
    }
    into(layout.buildDirectory.dir("docker"))
}

tasks.register<Copy>("copyEclipseLink") {
    from(eclipselinkModule) {
        include("eclipselink-*.jar")
        rename { "eclipselink.jar" }
    }
    into(layout.buildDirectory.dir("docker"))
}
