plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.example.weathersdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BASE_URL", "\"https://api.weatherbit.io/v2.0/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}
publishing {
    publications {
        create<MavenPublication>("release") {

            groupId = "com.example.weathersdk"
            artifactId = "weathersdk"
            version = "1.0.1"
            artifact("$buildDir/outputs/aar/WeatherSDK-release.aar")
        }
    }
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/HarrisDas/WeatherSDKApp/")
            credentials {
                username = System.getenv("GH_USERNAME")
                password = System.getenv("GH_PASSWORD")
            }
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.lifecycleViewModel)
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.20")


    implementation(libs.appCompat)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.gson)

    testImplementation(libs.mockk)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.espresso.core)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}