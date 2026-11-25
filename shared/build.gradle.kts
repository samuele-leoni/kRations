import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)                 // ← crea le configurazioni kspAndroid, kspIos…
    alias(libs.plugins.androidx.room3)      // ← plugin di Room 3 (schemi)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildKonfig)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    require(file.exists()) { "Create local.properties with SUPABASE_URL and SUPABASE_PUBLISHABLE_KEY" }
    file.inputStream().use(::load)
}
val supabaseUrl = localProperties.getProperty("SUPABASE_URL")?.trim().orEmpty()
val supabasePublishableKey = localProperties.getProperty("SUPABASE_PUBLISHABLE_KEY")?.trim().orEmpty()
require(supabaseUrl.isNotBlank()) { "SUPABASE_URL is missing from local.properties" }
require(supabasePublishableKey.isNotBlank()) { "SUPABASE_PUBLISHABLE_KEY is missing from local.properties" }

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
//    js {
//        browser()
//        binaries.executable()
//    }
//    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)   // ← import spostato qui
//    wasmJs {
//        browser()
//        binaries.executable()
//    }
    android {
        namespace = "com.example.krations.shared"   // ← da cambiare con il package definitivo
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // DB
            implementation(libs.androidx.room3.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // DI
            implementation(libs.koin.core)                     // ←
            implementation(libs.koin.compose.viewmodel)        // ← koinViewModel()

            // Supabase
            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)

            // Icons
            implementation(libs.icons.material.symbols.rounded.cmp)

            // Navigation
            implementation(libs.jetbrains.navigation3.ui)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // Logging
            implementation(libs.kermit)

            // Notifications
            implementation(libs.alarmee)

            // Permissions
            implementation(libs.calf.permissions.core)
            implementation(libs.calf.permissions.notifications)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
//        jsMain.dependencies {
//            implementation(libs.wrappers.browser)
//        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }
}

buildkonfig {
    packageName = "com.example.krations.core.config"
    defaultConfigs {
        buildConfigField(STRING, "SUPABASE_URL", supabaseUrl)
        buildConfigField(STRING, "SUPABASE_PUBLISHABLE_KEY", supabasePublishableKey)
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
}