plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)


    defaultConfig {
        applicationId = "ru.spb.yakovlev.stocksmonitor"
        buildToolsVersion = "30.0.3"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }


    flavorDimensions("serverType")
    productFlavors {
        create("serverType") {
            buildConfigField("String", "FIN_HUB_BASE_URL", "\"https://finnhub.io/api/v1/\"")
            buildConfigField("String", "MBOUM_BASE_URL", "\"https://mboum.com/api/v1\"")
            buildConfigField("String", "WEB_SOCKET", "\"wss://ws.finnhub.io\"")
            buildConfigField("String", "FIN_HUB_API_KEY", "\"c19sl0v48v6tl8v9p8fg\"")
            buildConfigField("String", "MBOUM_API_KEY", "\"dH605diy5u4ch51yYUsFV8BmRNMXDGHzVLsJkQgxo5gRiyEN1DtiGb7GG8eJ\"")
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
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.6.0-alpha01")

    // UI
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    // Activity KTX
    implementation("androidx.activity:activity-ktx:1.3.0-alpha05")

    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.3.2")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigationVersion"]}")

    // Lifecycle
    val lifecycleVersion = "2.4.0-alpha01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    // DI
    val daggerVersion = "2.33"
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hiltVersion"]}")
    val androidxHilt = "1.0.0-beta01"
    //implementation("androidx.hilt:hilt-lifecycle-viewmodel:$androidxHilt")
    kapt("androidx.hilt:hilt-compiler:$androidxHilt")
    implementation("androidx.hilt:hilt-work:$androidxHilt")

    // Concurrency
    val coroutinesVersion = "1.4.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Image downloading
    implementation("io.coil-kt:coil:1.1.1")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    // DB
    val roomVersion = "2.2.6"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // WorkManager
    val workVersion = "2.5.0"
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    androidTestImplementation("androidx.work:work-testing:$workVersion")

    // Retrofit + OkHttp
    val okHttpVersion = "5.0.0-alpha.2"
    val retofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // Moshi
    val moshiVersion = "1.11.0"
    implementation ("com.squareup.retrofit2:converter-moshi:$retofitVersion")
    implementation ("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    // ViewBindingPropertyDelegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.5")

    // WebSocket
    implementation ("org.java-websocket:Java-WebSocket:1.5.1")


    // kotlinx-datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")

    // MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
