plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android.gradle.plugin)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.weiyou.attractions"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.weiyou.attractions"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.weiyou.attractions.HiltTestRunner"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // 新增UI TEST
    androidTestImplementation(libs.androidx.espresso.intents)
// Hilt for testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
// Add the necessary Hilt testing dependencies
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

// AndroidX ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

// Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

// LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

// OkHttp Logging Interceptor
    implementation(libs.okhttp.logging.interceptor)

// DataStore
    implementation(libs.androidx.datastore.preferences)

// Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

// Unit test dependencies
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
}

kapt {
    correctErrorTypes = true
}