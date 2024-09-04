plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.huawei.agconnect")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.sitekittest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sitekittest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.google.code.gson:gson:2.11.0")

    //Google UI Button
    implementation("com.github.shobhitpuri:custom-google-signin-button:2.0.0")

    //For chrome custom tabs
    implementation ("androidx.browser:browser:1.5.0")

    //Firebase Auth SDK
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0") // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
}