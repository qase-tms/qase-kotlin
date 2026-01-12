# Qase TMS Kotlin Android Integration

Publish your Android test results easily and effectively with Qase TMS.

## Installation

To install the latest release version, follow the instructions below.

### Gradle

Add the following dependencies to your `build.gradle.kts`:

```kotlin
dependencies {
    androidTestImplementation("io.qase:qase-kotlin-android:1.0.1")
}
```

## Getting Started

The Qase Android integration can auto-generate test cases and suites based on your test data. Test results from subsequent runs will match the same test cases as long as their names and file paths remain unchanged.

You can also annotate tests with IDs of existing test cases from Qase.io before execution. This approach ensures a reliable binding between your automated tests and test cases, even if you rename, move, or parameterize your tests.

### Usage Options

There are two main ways to use Qase Android integration:

#### Option 1: Using Custom Test Runner (Recommended)

Configure your `build.gradle.kts` to use `QaseAndroidJUnitRunner`:

```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "io.qase.commons.kotlin.android.runners.QaseAndroidJUnitRunner"
    }
}
```

#### Option 2: Using QaseAndroidJUnit4 Runner

Use `QaseAndroidJUnit4` as your test runner:

```kotlin
@RunWith(QaseAndroidJUnit4::class)
class ExampleTest {
    // ...
}
```

#### Option 3: MultiDex Support

If your app uses MultiDex, use `MultiDexQaseAndroidJUnitRunner`:

```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "io.qase.commons.kotlin.android.runners.MultiDexQaseAndroidJUnitRunner"
    }
}
```

### Metadata Annotations and Methods

- **`@QaseId(value: Long)`**: Set the ID of the test case.
- **`@QaseIds(vararg value: Long)`**: Set multiple IDs for the test case.
- **`@QaseTitle(value: String)`**: Set the title of the test case.
- **`@QaseField(key: String, value: String)`**: Set custom fields for the test case.
- **`@QaseParameter(key: String, value: String)`**: Specify the parameters for the test case.
- **`@QaseIgnore`**: Ignore the test case. The test will execute, but the results will not be reported to Qase.io.
- **`Qase.comment(comment: String)`**: Add a comment to the test case.

### Example Test Case

Here's a simple example of using Qase in an Android test:

```kotlin
package com.example.androidtest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.qase.commons.kotlin.junit4.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    
    @Test
    @QaseId(123L)
    @QaseTitle("Verify app context")
    fun useAppContext() {        
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.androidtest", appContext.packageName)
        
        Qase.comment("App context is correct")
    }
    
    @Test
    @QaseIds(456L, 789L)
    fun testWithMultipleIds() {
        Qase.title("Multi-project test")
        // Your test code here
    }
}
```

To execute your Android tests and report the results to Qase.io, use the following commands:

#### Gradle

```bash
./gradlew connectedAndroidTest
```

## Usage

The Android reporter does not have configuration parameters. After completing the tests, you can find the results in
JSON format in the
`/build/outputs/connected_android_test_additional_output/debugAndroidTest/connected/{your_device}/qase-results`
directory.

The results are stored in a specific format designed for uploading using the [QaseCtl utility](https://github.com/qase-tms/qasectl). To upload results:

```bash
qasectl testops result upload \
  --project PROJ \
  --token <token> \
  --id 1 \
  --format qase \
  --path ./qase-results/results/ \
  --verbose
```

## Requirements

- **Android**: Min SDK 21 or higher
- **Java**: Version 17 or higher
- **AndroidX Test**: Required for Android instrumentation tests

## Robolectric Support

The library automatically detects Robolectric tests and uses file-based storage instead of TestStorage. No additional configuration is needed.

## External Storage Permissions

The `QaseAndroidJUnitRunner` automatically grants necessary permissions for TestStorage. If you're using a custom runner, you may need to add `ExternalStoragePermissionsListener` manually.

For further assistance, please refer to the [Qase Authentication Documentation](https://developers.qase.io/#authentication).
