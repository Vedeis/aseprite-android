plugins {
    id("com.android.application")
}

android {
    namespace = "org.aseprite.androidproto"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.aseprite.androidproto"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.3.17.2-native-proto"
    }
}
