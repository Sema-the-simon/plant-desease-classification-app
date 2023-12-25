plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.plantdisease"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plantdisease"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation("androidx.core:core-ktx:1.9.0")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //coil library
    implementation("io.coil-kt:coil-compose:2.1.0")

    //compose
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation ("androidx.navigation:navigation-compose:2.7.5")

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))

    //hilt
    implementation("androidx.hilt:hilt-work:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")


    //test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    //cameraX
    val camerax_version = "1.3.0"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

    //tensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.0")
    implementation("org.tensorflow:tensorflow-lite-gpu-delegate-plugin:0.4.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")


    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}