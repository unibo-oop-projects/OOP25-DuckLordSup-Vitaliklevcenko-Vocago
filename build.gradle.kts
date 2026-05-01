plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("it.unibo.vocago.main")
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
