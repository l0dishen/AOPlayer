plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

kotlin {
    jvmToolchain(8)
}

android {
    namespace = "com.skyrecords.aoplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skyrecords.aoplayer"
        minSdk = 21
        targetSdk = 34
        versionCode = 11
        versionName = "1.0.0"

        buildConfigField(
            type = "String",
            name = "VERSION_NAME",
            value = "\"1.0.0\""
        )
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



dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.legacy.support)
    implementation(libs.glide)
    implementation(libs.androidx.media)
    implementation(libs.gson)
    implementation(libs.verticalseekbar)
    implementation(libs.transport.api)

    testImplementation(libs.junit)
}
