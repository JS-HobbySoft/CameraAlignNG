# ProGuard / R8 keep rules for CameraAlignNG

# Keep application package classes
-keep class org.jshobbysoft.cameraalignng.** { *; }

# Keep CameraX components
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Keep Coil image loader
-keep class coil.** { *; }
-dontwarn coil.**

# Keep Jetpack Compose runtime & view interop
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
