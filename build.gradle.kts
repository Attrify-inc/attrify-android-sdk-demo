import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

extra.apply {
    val properties = Properties()
    try {
        rootProject.file("local.properties").inputStream().use { properties.load(it) }
    } catch (ignored: Throwable) {
    }

    val measurementId = properties.getProperty("measurementId", "<YOUR_MEASUREMENT_ID>")
    set("measurementId", measurementId)

    val forceUseVersion = properties.getProperty("forceUseVersion", "")
    set("forceUseVersion", forceUseVersion)

    println("measurementId: $measurementId, forceUseVersion: $forceUseVersion")
}

subprojects {
    val forceUseVersion = rootProject.extra.get("forceUseVersion") as? String?
    if (forceUseVersion.isNullOrBlank()) {
        return@subprojects
    }
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            all {
                val selector = requested
                if (selector !is ModuleComponentSelector){
                    return@all
                }
                if (selector.group == "com.attrify" && selector.module == "attrify-sdk") {
                    useTarget("${selector.group}:${selector.module}:${forceUseVersion}")
                    println("Attrify SDK using force version: $forceUseVersion")
                }
            }
        }
    }
}