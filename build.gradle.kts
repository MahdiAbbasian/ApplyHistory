// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.android.library") version "8.2.2" apply false
}