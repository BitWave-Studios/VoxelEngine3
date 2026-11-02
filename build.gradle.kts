import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

group = property("group") as String
version = property("version") as String


repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val lwjglVersion = property("lwjglVersion") as String
val os = OperatingSystem.current()
val arch = System.getProperty("os.arch").lowercase()
val lwjglNatives = when {
    os.isMacOsX -> if (arch == "aarch64" || arch == "arm64") "natives-macos-arm64" else "natives-macos"
    os.isLinux -> if (arch == "aarch64" || arch == "arm64") "natives-linux-arm64" else "natives-linux"
    os.isWindows -> if (arch == "aarch64" || arch == "arm64") "natives-windows-arm64" else "natives-windows"
    else -> throw GradleException("Unsupported OS: ${os.name}, arch: $arch")
}

dependencies {
    implementation("org.joml:joml:1.10.8")

    implementation(platform("org.lwjgl:lwjgl-bom:${property("lwjglVersion")}"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("org.bitwave.ve3.demo.DemoGame")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
