# Qase TMS Kotlin JUnit 4 Integration

JUnit 4 integration for Qase TMS Kotlin reporters. This module provides annotations and runners for easy integration with JUnit 4 tests.

## Installation

Add the following dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    testImplementation("io.qase:qase-kotlin-junit4:1.0.0")
    testImplementation("io.qase:qase-kotlin-commons:1.0.0")
}
```

## Features

- **Custom Runners**: `QaseRunner` and `QaseParametrizedRunner` for automatic test result collection
- **Helper Object**: `Qase` object for programmatic test metadata management
- **Automatic Integration**: Works seamlessly with existing JUnit 4 tests

## Usage

### Option 1: Using Custom Runner (Recommended)

Use `QaseRunner` as your test runner:

```kotlin
import io.qase.commons.kotlin.junit4.QaseRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(QaseRunner::class)
class ExampleTest {
    
    @Test
    fun testExample() {
        // Test code
    }
}
```

### Option 2: Using QaseJunit4Listener Manually

Add the listener manually to your test runner:

```kotlin
import io.qase.commons.kotlin.listener.QaseJunit4Listener
import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner

class CustomRunner(clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {
    override fun run(notifier: RunNotifier) {
        notifier.addListener(QaseJunit4Listener())
        super.run(notifier)
    }
}
```

### Option 3: Using Annotations

You can use annotations to set test metadata declaratively:

```kotlin
import io.qase.commons.kotlin.junit4.*
import org.junit.Test
import org.junit.runner.RunWith
import io.qase.commons.kotlin.junit4.QaseRunner

@RunWith(QaseRunner::class)
class ExampleTest {
    
    @Test
    @QaseId(123L)
    @QaseTitle("Custom test title")
    @QaseField(key = "priority", value = "high")
    @QaseField(key = "component", value = "auth")
    @QaseParameter(key = "browser", value = "chrome")
    fun testExample() {
        Qase.comment("Test comment")  // Use method for comments
        // ... test code
    }
    
    @Test
    @QaseIds(123L, 456L, 789L)  // Multiple IDs
    @QaseIgnore  // Ignore this test in reporting
    fun testIgnored() {
        // ... test code
    }
}
```

Available annotations:
- **`@QaseId(value: Long)`**: Set a single test case ID
- **`@QaseIds(vararg value: Long)`**: Set multiple test case IDs
- **`@QaseTitle(value: String)`**: Set the test case title
- **`@QaseField(key: String, value: String)`**: Add a custom field (repeatable)
- **`@QaseParameter(key: String, value: String)`**: Add a parameter (repeatable)
- **`@QaseIgnore`**: Ignore the test case in reporting

Note: Comments should be added using `Qase.comment()` method, not via annotation.

### Option 4: Using Qase Helper Object

You can also programmatically set test metadata using the `Qase` helper object:

```kotlin
import io.qase.commons.kotlin.junit4.Qase
import org.junit.Test
import org.junit.runner.RunWith
import io.qase.commons.kotlin.junit4.QaseRunner

@RunWith(QaseRunner::class)
class ExampleTest {
    
    @Test
    fun testExample() {
        Qase.id(123L)  // Set test case ID
        Qase.title("Custom test title")  // Override test title
        Qase.comment("Test comment")  // Add comment
        Qase.fields(mapOf("key1" to "value1"))  // Add custom fields
        Qase.parameters(mapOf("param1" to "value1"))  // Add parameters
        // ... test code
        
        // To ignore this test in reporting:
        // Qase.ignore()
    }
}
```

Available methods:
- **`Qase.id(id: Long)`**: Set the ID of the test case
- **`Qase.ids(vararg ids: Long)`**: Set multiple IDs for the test case
- **`Qase.title(title: String)`**: Set the title of the test case
- **`Qase.fields(fields: Map<String, String>)`**: Set custom fields for the test case
- **`Qase.parameters(params: Map<String, String>)`**: Specify the parameters for the test case
- **`Qase.ignore()`**: Ignore the test case. The test will execute, but the results will not be reported
- **`Qase.comment(comment: String)`**: Add a comment to the test case

### Option 5: Parameterized Tests

Use `QaseParametrizedRunner` for parameterized tests:

```kotlin
import io.qase.commons.kotlin.junit4.QaseParametrizedRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(QaseParametrizedRunner::class)
class ParameterizedTest(private val input: Int, private val expected: Int) {
    
    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(1, 2),
                arrayOf(2, 4),
                arrayOf(3, 6)
            )
        }
    }
    
    @Test
    fun testMultiplication() {
        assertEquals(expected, input * 2)
    }
}
```

## Complete Example

Here's a complete example using the Qase helper object:

```kotlin
import io.qase.commons.kotlin.junit4.Qase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import io.qase.commons.kotlin.junit4.QaseRunner

@RunWith(QaseRunner::class)
class ExampleTest {
    
    @Test
    fun testUserRegistration() {
        Qase.id(123L)
        Qase.ids(123L, 456L, 789L)  // Add multiple IDs
        Qase.title("User Registration Test")
        Qase.fields(mapOf("priority" to "high", "component" to "auth"))
        Qase.parameters(mapOf("environment" to "staging"))
        
        // Test code
        val result = registerUser("test@example.com")
        assertEquals(true, result)
        
        Qase.comment("Registration successful")
    }
    
    @Test
    fun testIgnoredExample() {
        Qase.ignore()  // This test will execute but won't be reported
        // Test code
    }
    
    private fun registerUser(email: String): Boolean {
        // Implementation
        return true
    }
}
```

## Test Results

Test results are saved in Qase format to the `qase-results` directory:

- **Results**: `qase-results/results/{test-id}.json`
- **Attachments**: `qase-results/attachments/{attachment-id}.{ext}`

Results include:
- Test metadata (title, IDs, custom fields)
- Execution details (status, duration, stacktrace)
- Steps hierarchy
- Attachments references
- Custom fields and parameters

## Requirements

- **Java**: Version 17 or higher
- **JUnit**: Version 4.13.2 or higher
- **Kotlin**: Version 1.9.24 or higher

## Compatibility

This module is compatible with:
- Standard JUnit 4 tests
- Parameterized tests
- Test suites
- Custom test runners

For Android tests, use `qase-kotlin-android` module instead, which provides Android-specific runners and utilities.

