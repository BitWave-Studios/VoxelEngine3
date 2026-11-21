import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

group = property("group") as String
version = property("version") as String
val releaseType = property("releaseType") as String


repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val os = OperatingSystem.current()
val arch = System.getProperty("os.arch").lowercase()

val lwjglVersion = property("lwjglVersion") as String
val lwjglNatives = when {
    os.isMacOsX -> if (arch == "aarch64" || arch == "arm64") "natives-macos-arm64" else "natives-macos"
    os.isLinux -> if (arch == "aarch64" || arch == "arm64") "natives-linux-arm64" else "natives-linux"
    os.isWindows -> if (arch == "aarch64" || arch == "arm64") "natives-windows-arm64" else "natives-windows"
    else -> throw GradleException("Unsupported OS: ${os.name}, arch: $arch")
}


dependencies {
    implementation(libs.joml)
    implementation(libs.joml.primitives)
    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)

    implementation(platform("org.lwjgl:lwjgl-bom:${property("lwjglVersion")}"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opengles")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-egl")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengles::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-egl::$lwjglNatives")

    testImplementation(platform(libs.junit))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("org.bitwave.ve3.demo.DemoGame")
}

tasks.register("createVersionProperties") {
    description = "Creates a version.properties file with the project version."

    val versionFile = layout.projectDirectory.file("src/main/resources/version.properties")
    outputs.file(versionFile)

    doLast {
        versionFile.asFile.apply {
            parentFile.mkdirs()
            writeText(
                """
                publisher=${project.group}
                version=${project.version}
                releaseType=${releaseType}
                """.trimIndent()
            )
        }
    }
}

tasks.named("processResources") {
    dependsOn("createVersionProperties")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
