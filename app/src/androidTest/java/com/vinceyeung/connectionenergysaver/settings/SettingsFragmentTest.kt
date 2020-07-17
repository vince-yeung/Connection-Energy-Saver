package com.vinceyeung.connectionenergysaver.settings

import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.vinceyeung.connectionenergysaver.MainActivity
import com.vinceyeung.connectionenergysaver.R
import com.vinceyeung.connectionenergysaver.utilities.*
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test


class SettingsFragmentTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkBluetoothTitle() {
        onView(withId(R.id.bluetooth_title_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.bluetooth_title_text_view)).check(matches(withText(R.string.bluetooth_title)))
    }

    @Test
    fun checkBluetoothLabels() {
        onView(withId(R.id.bluetooth_switch_label)).check(matches(isDisplayed()))
        onView(withId(R.id.bluetooth_switch_label)).check(matches(withText(R.string.bluetooth_toggle_label)))

        onView(withId(R.id.bluetooth_timer_label)).check(matches(isDisplayed()))
    }

    @Test
    fun checkBluetoothNotSupported() {
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            onView(withId(R.id.bluetooth_enable_switch)).check(matches(not(isEnabled())))
            onView(withId(R.id.bluetooth_enable_switch)).check(matches(isNotChecked()))

            val defaultTimerLabel =
                InstrumentationRegistry.getInstrumentation().targetContext.resources.getQuantityString(
                    R.plurals.bluetooth_timer_label,
                    BLUETOOTH_DEFAULT_TIMER,
                    BLUETOOTH_DEFAULT_TIMER
                )
            onView(withId(R.id.bluetooth_timer_label)).check(matches(withText(defaultTimerLabel)))
            onView(withId(R.id.bluetooth_timer_seek_bar)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun checkBluetoothSupported() {
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            onView(withId(R.id.bluetooth_enable_switch)).check(matches(isEnabled()))
            onView(withId(R.id.bluetooth_timer_seek_bar)).check(matches(isEnabled()))

            val sharedPreferences =
                InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
                    PREFERENCE_FILE_NAME, Context.MODE_PRIVATE
                )

            var testCycles = 5
            while (testCycles-- > 0) {
                when (sharedPreferences.getBoolean(
                    BLUETOOTH_ENABLED_KEY,
                    BLUETOOTH_DEFAULT_IS_ENABLED
                )) {
                    true -> onView(withId(R.id.bluetooth_enable_switch)).check(matches(isChecked()))
                    false -> onView(withId(R.id.bluetooth_enable_switch)).check(
                        matches(
                            not(
                                isChecked()
                            )
                        )
                    )
                }

                onView(withId(R.id.bluetooth_enable_switch)).perform(click())
            }

            val clickLocation =
                listOf(
                    GeneralLocation.CENTER_LEFT,
                    GeneralLocation.CENTER,
                    GeneralLocation.CENTER_RIGHT
                )
            for (location in clickLocation) {
                onView(withId(R.id.bluetooth_timer_seek_bar)).perform(
                    GeneralClickAction(
                        Tap.SINGLE, location, Press.FINGER, 0, 0
                    )
                )

                val savedTimer =
                    sharedPreferences.getInt(BLUETOOTH_TIMER_KEY, BLUETOOTH_DEFAULT_TIMER)
                val timerLabel =
                    InstrumentationRegistry.getInstrumentation().targetContext.resources.getQuantityString(
                        R.plurals.bluetooth_timer_label, savedTimer, savedTimer
                    )

                onView(withId(R.id.bluetooth_timer_label)).check(matches(withText(timerLabel)))
            }
        }
    }
}