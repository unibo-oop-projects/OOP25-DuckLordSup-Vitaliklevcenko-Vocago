plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
    id("org.danilopianini.gradle-java-qa") version "1.164.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("it.unibo.vocago.VocagoApp")
}

sourceSets {
    main {
        resources {
            setSrcDirs(listOf("src/main/data"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
