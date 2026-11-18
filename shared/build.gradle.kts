import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)                 // ← crea le configurazioni kspAndroid, kspIos…
    alias(libs.plugins.androidx.room3)      // ← plugin di Room 3 (schemi)
    alias(libs.plugins.kotlinSerialization) //
}

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

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
}