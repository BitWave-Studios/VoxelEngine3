plugins {
    application
}

dependencies {
    implementation(project(":core"))
    implementation(project(":facet"))
    implementation(project(":ve3-desktop"))
}

application {
    mainClass.set("org.bitwave.ve3demo.DemoGame")
}
