plugins {
    id("java-library")
}

dependencies {
    api(project(":core"))
    api(project(":facet"))

    implementation(platform("org.lwjgl:lwjgl-bom:${property("lwjglVersion")}"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    // Add runtimeOnly dependencies for native libraries based on the operating system
    val osName = System.getProperty("os.name").lowercase()
    val lwjglNatives = when {
        osName.contains("windows") -> "natives-windows"
        osName.contains("mac") -> "natives-macos"
        osName.contains("linux") -> "natives-linux"
        else -> throw GradleException("Unsupported operating system: $osName")
    }
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
