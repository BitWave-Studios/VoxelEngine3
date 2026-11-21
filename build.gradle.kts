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
val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unix").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
        else if (arch.startsWith("ppc"))
            "natives-linux-ppc64le"
        else if (arch.startsWith("riscv"))
            "natives-linux-rsv64"
        else
            "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) } ->
                if (arch.contains("64"))
                    "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        else
            "natives-windows-x86"
        else ->
            throw Error("Unrecognised or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}


dependencies {
    implementation(libs.joml)
    implementation(libs.joml.primitives)
    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)

    implementation(platform("org.lwjgl:lwjgl-bom:${lwjglVersion}"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opengles")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-assimp")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengles::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")

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
