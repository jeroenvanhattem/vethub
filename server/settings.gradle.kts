rootProject.name = "vethub-server"
pluginManagement {
    val springBootPluginVersion: String by settings
    val springDependencyPluginVersion: String by settings
    val qualityPluginVersion: String by settings
    val spotbugsPluginVersion: String by settings
    val lombokPluginVersion: String by settings
    val spotlessPluginVersion: String by settings
    val dependencyUpdatesPluginVersion: String by settings
    plugins {
        id("org.springframework.boot") version springBootPluginVersion
        id("io.spring.dependency-management") version springDependencyPluginVersion
        id("ru.vyarus.quality") version qualityPluginVersion
        id("com.github.spotbugs") version spotbugsPluginVersion
        id("io.freefair.lombok") version lombokPluginVersion
        id("com.diffplug.spotless") version spotlessPluginVersion
        id("com.github.ben-manes.versions") version dependencyUpdatesPluginVersion
    }
}
