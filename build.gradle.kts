plugins {
    id("java")
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = property("group") as String
    version = property("version") as String
}

subprojects {
    apply {
        plugin("java")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:all")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
