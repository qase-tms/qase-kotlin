# Qase TMS Kaspresso Reporter

Publish your test results easily and effectively with Qase TMS.

## Installation

To install the latest release version (1.0.x), follow the instructions below Gradle.

### Gradle

Add the following dependencies to your `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.kaspresso"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kaspresso"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.kaspersky.kaspresso.runner.KaspressoRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.androidXTestExtJunitKtx)
    androidTestImplementation(libs.androidXTestExtJunit)
    androidTestImplementation(libs.junit)
    androidTestUtil(libs.androidXTestOrchestrator)

    androidTestImplementation("io.qase:qase-kaspresso-reporter:1.0.0")
}
```

## Getting Started

The Kaspresso reporter can auto-generate test cases and suites based on your test data. Test results from subsequent
runs
will match the same test cases as long as their names and file paths remain unchanged.

You can also annotate tests with IDs of existing test cases from Qase.io before execution. This approach ensures a
reliable binding between your automated tests and test cases, even if you rename, move, or parameterize your tests.

### Metadata Annotations

- **`Qase.id`**: Set the ID of the test case.
- **`Qase.title`**: Set the title of the test case.
- **`Qase.fields`**: Set custom fields for the test case.
- **`Qase.parameters`**: Specify the parameters for the test case.
- **`Qase.ignore`**: Ignore the test case. The test will execute, but the results will not be reported to Qase.io.
- **`Qase.comment`**: Add a comment to the test case.

For detailed instructions on using annotations and methods, refer to [Usage](./docs/usage.md).

### Example Test Case

Here’s a simple example of using Qase annotations in a Kaspresso test:

```kotlin
package com.example.kaspresso

import androidx.test.platform.app.InstrumentationRegistry
import io.qase.kaspresso.withQaseSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.qase.kaspresso.Qase

import org.junit.Test
import org.junit.Assert.*

class QaseSupportTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withQaseSupport()
) {

    @Test
    fun useAppContext() {
        Qase.id(123)
        Qase.title("Example test")
        Qase.fields(mapOf("key1" to "value1", "key2" to "value2"))
        Qase.parameters(mapOf("param1" to "value1", "param2" to "value2"))

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kaspresso", appContext.packageName)
        Qase.comment("Some comment")
    }
}
```

To execute your Kaspresso tests and report the results to Qase.io, use the following commands:

#### Gradle

```bash
gradle test
```

You can try it with the example project at [`examples/kaspresso`](../examples/kaspresso/).

## Usage

The Kaspresso reporter does not have configuration parameters. After completing the tests, you can find the results in
JSON format in the
`/build/outputs/connected_android_test_additional_output/debugAndroidTest/connected/{your_device}/qase-results`
directory.

The results are stored in a specific format designed for uploading using
the [QaseCtl utility](https://github.com/qase-tms/qasectl). To do this, execute the following command:

```bash
qasectl testops result upload --project PROJ --token <token> --id 1 --format qase --path /build/outputs/connected_android_test_additional_output/debugAndroidTest/connected/{your_device}/qase-results/results/ --verbose
```

## Requirements

- **Kaspresso**: Version 1.5.5 or higher is required.
- **Java**: Version 17 or higher is required.

For further assistance, please refer to
the [Qase Authentication Documentation](https://developers.qase.io/#authentication).
