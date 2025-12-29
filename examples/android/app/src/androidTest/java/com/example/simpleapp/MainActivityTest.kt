package com.example.simpleapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.qase.commons.kotlin.Description
import io.qase.commons.kotlin.Step
import io.qase.commons.kotlin.android.runners.QaseAndroidJUnit4
import io.qase.commons.kotlin.junit4.Qase
import io.qase.commons.kotlin.junit4.QaseField
import io.qase.commons.kotlin.junit4.QaseIds
import io.qase.commons.kotlin.junit4.QaseParameter
import io.qase.commons.kotlin.junit4.QaseTitle
import io.qase.commons.kotlin.step
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(QaseAndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    @QaseTitle("Check welcome text display")
    @QaseField("priority", "high")
    @QaseField("component", "ui")
    @QaseIds(123)
    fun testWelcomeTextIsDisplayed() {
        step("Check that welcome text is displayed") {
            checkWelcomeTextDisplayed()
        }
        
        step("Check welcome text content") {
            checkWelcomeTextContent()
        }

        Qase.comment("Welcome text is displayed successfully")
    }

    @Step("Check that welcome text is displayed")
    private fun checkWelcomeTextDisplayed() {
        onView(withId(R.id.welcomeTextView))
            .check(matches(isDisplayed()))
    }

    @Step("Check welcome text content")
    private fun checkWelcomeTextContent() {
        onView(withId(R.id.welcomeTextView))
            .check(matches(withText("Welcome!")))
    }

    @Test
    @QaseTitle("Check initial counter value")
    @QaseField("priority", "medium")
    @QaseField("component", "counter")
    @QaseIds(124,125,126)
    fun testCounterStartsAtZero() {
        step("Check counter value") {
            checkCounterValue(0)
        }
    }

    @Step("Check counter value: {expectedValue}")
    private fun checkCounterValue(expectedValue: Int) {
        onView(withId(R.id.counterTextView))
            .check(matches(withText("Counter: $expectedValue")))
    }

    @Test
    @QaseTitle("Check counter increment on button click")
    @QaseField("priority", "high")
    @QaseField("component", "counter")
    @QaseParameter("expected_clicks", "3")
    fun testClickButtonIncrementsCounter() {
        step("First button click") {
            clickIncrementButton()
            checkCounterValue(1)
        }
        
        step("Second button click") {
            clickIncrementButton()
            checkCounterValue(2)
        }
        
        step("Third button click") {
            clickIncrementButton()
            checkCounterValue(3)
        }

        Qase.comment("Counter successfully increments on each click")
    }

    @Step("Click increment button")
    private fun clickIncrementButton() {
        onView(withId(R.id.clickButton))
            .perform(click())
    }

    @Test
    @QaseTitle("Check counter reset")
    @QaseField("priority", "high")
    @QaseField("component", "counter")
    fun testResetButtonResetsCounter() {
        
        step("Increment counter to 3") {
            clickIncrementButton()
            clickIncrementButton()
            clickIncrementButton()
            checkCounterValue(3)
        }
        
        step("Reset counter") {
            clickResetButton()
            checkCounterValue(0)
        }

        Qase.comment("Counter successfully resets to zero")
    }

    @Step("Click reset button")
    private fun clickResetButton() {
        onView(withId(R.id.resetButton))
            .perform(click())
    }

    @Test
    @QaseTitle("Check all buttons display")
    @QaseField("priority", "medium")
    @QaseField("component", "ui")
    fun testAllButtonsAreDisplayed() {
        step("Check increment button") {
            checkButtonDisplayed(R.id.clickButton)
        }
        
        step("Check reset button") {
            checkButtonDisplayed(R.id.resetButton)
        }
    }

    @Step("Check button display with id: {buttonId}")
    private fun checkButtonDisplayed(buttonId: Int) {
        onView(withId(buttonId))
            .check(matches(isDisplayed()))
    }

    @Test
    @QaseTitle("Check counter TextView display")
    @QaseField("priority", "medium")
    @QaseField("component", "ui")
    fun testCounterTextViewIsDisplayed() {
        step("Check TextView display") {
            checkCounterTextViewDisplayed()
        }
    }

    @Step("Check counter TextView display")
    private fun checkCounterTextViewDisplayed() {
        onView(withId(R.id.counterTextView))
            .check(matches(isDisplayed()))
    }
}

