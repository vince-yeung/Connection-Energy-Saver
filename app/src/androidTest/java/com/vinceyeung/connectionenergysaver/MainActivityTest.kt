package com.vinceyeung.connectionenergysaver

import android.bluetooth.BluetoothAdapter
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get: Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun checkToolbarVisible() {
        onView(isAssignableFrom(Toolbar::class.java)).check(matches(isDisplayed()))
    }

    @Test
    fun checkToolbarTitle() {
        onView(allOf(instanceOf(TextView::class.java), withParent(instanceOf(Toolbar::class.java)))).check(
            matches(withText(R.string.app_name))
        )
    }

    @Test
    fun checkBluetoothSupportedToast() {
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            onView(withText(R.string.error_no_bluetooth_support)).inRoot(withDecorView(not(activityTestRule.activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testBackButton() {
        onView(isRoot()).perform(ViewActions.pressBackUnconditionally())
        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }
}