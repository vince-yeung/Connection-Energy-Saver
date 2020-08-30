package com.vinceyeung.connectionenergysaver

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.vinceyeung.connectionenergysaver.settings.SettingsRepository
import com.vinceyeung.connectionenergysaver.settings.SettingsViewModel
import com.vinceyeung.connectionenergysaver.utilities.PREFERENCE_FILE_NAME
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {

    private var viewModel: SettingsViewModel? = null

    @Mock
    private var repository: SettingsRepository? = null

    @Before
    @Throws(NullPointerException::class)
    fun setup() {
        val applicationContext = InstrumentationRegistry.getInstrumentation().context
        val sharedPreferences = applicationContext.getSharedPreferences(
            PREFERENCE_FILE_NAME, MODE_PRIVATE
        )

        repository = SettingsRepository(sharedPreferences)

        repository?.let { repo ->
            viewModel = SettingsViewModel(applicationContext as Application, repo)
        } ?: run {
            throw NullPointerException("Repository is null")
        }
    }

    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    @Test
    fun isCheckingBluetoothTest() {
        val isCheckingBluetooth = repository?.isCheckingBluetooth()
        assertEquals(isCheckingBluetooth, viewModel?.isCheckingBluetooth())
    }

    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    @Test
    fun getBluetoothTimerTest() {
        val bluetoothTimer = repository?.getBluetoothTimer()
        assertEquals(bluetoothTimer, viewModel?.getBluetoothTimer())
    }

    @After
    fun tearDown() {
        viewModel = null
        repository = null
    }
}