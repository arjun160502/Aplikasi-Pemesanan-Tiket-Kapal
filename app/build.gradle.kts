plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.junel.tugasprojekarjunelin"
    compileSdk = 35 // Sudah benar

    defaultConfig {
        applicationId = "com.junel.tugasprojekarjunelin"
        minSdk = 26
        targetSdk = 35 // Sudah benar
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Perbaikan: vectorDrawables (bukan vectorDrawbles) dan pastikan di dalam defaultConfig


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
        // Perbaikan: Ubah ke JavaVersion.VERSION_1_8 atau JavaVersion.VERSION_17 jika Anda ingin menggunakan fitur Java yang lebih baru dan memiliki JDK yang sesuai
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    // Jika Anda menggunakan Kotlin, tambahkan blok kotlinOptions juga
    // kotlinOptions {
    //    jvmTarget = '1.8' // Sesuaikan dengan sourceCompatibility
    // }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}