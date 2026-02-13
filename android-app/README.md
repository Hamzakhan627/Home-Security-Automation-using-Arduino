# Job Expense Manager (Android, Material 3)

This directory now contains the **core source code** for an Android app that tracks expenses across multiple Jobs.

If you are asking _"how do I get this application running on my phone?"_, use the steps below.

## What is included

- Kotlin + MVVM structure
- Room entities / DAO / repository
- Compose Material 3 dashboard scaffold
- Expense and Credit separation
- Job-based independent ledgers
- Date rules utility + unit tests

## Quick start (Android Studio)

> This repo is not a complete Android Studio project yet (no Android Gradle wrapper / manifest scaffolding in this folder by default), so the fastest path is to create a new app and copy these files in.

1. **Install Android Studio** (latest stable).
2. **Create a new project**
   - Template: `Empty Activity`
   - Language: Kotlin
   - Min SDK: 26+
   - Name example: `JobExpenseManager`
3. In your new project, replace or copy source files from this repo's `android-app/app/src/main/java/...` into your app module under the same package path.
4. Copy tests from:
   - `android-app/app/src/test/java/com/example/jobexpensemanager/DateRulesTest.kt`
5. Add Room + Lifecycle + Compose dependencies to your app module `build.gradle.kts`.

## Required dependencies (app/build.gradle.kts)

Add (or align versions with your project BOM/plugin versions):

```kotlin
dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.10.1")

    // Lifecycle + ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Room
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Tests
    testImplementation("junit:junit:4.13.2")
}
```

And make sure plugins include:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}
```

## Running on device

1. Connect Android phone with USB debugging enabled (or start emulator).
2. In Android Studio, click **Run ▶**.
3. Select device and install.

## Build APK

- Debug APK: `Build > Build APK(s)`
- Release APK/AAB: `Build > Generate Signed Bundle / APK`

## Implemented business rules recap

- `remainingAmount = totalAmount - totalExpenses`
- Credits are separate and **do not** change remaining amount
- Each Job stores independent records
- Day number is derived from Job start date using `DateRules.dayNumberFromStart`

## Current status / limitations

This is still a **starter implementation** (not full production feature-complete app). You should still add:

- Navigation and job creation screens
- Edit/delete UI wiring for expenses and credits
- Better date pickers and formatted date display
- Input validation + error states
- Export / backup and authentication (if needed)
