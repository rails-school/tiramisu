# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/acadet/android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn **
-dontnote **

# Proguard
-keepnames public class * extends io.realm.RealmObject
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**

# Retrofit
-keep class com.google.gson.** { *; }
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keep class retrofit.* { *; }
-dontwarn rx.*
-keep class com.example.testobfuscation.** { *; }
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*
-keepclasseswithmembers class * { @retrofit.http.* <methods>; }