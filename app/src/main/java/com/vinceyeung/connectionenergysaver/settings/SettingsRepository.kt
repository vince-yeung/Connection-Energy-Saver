package com.vinceyeung.connectionenergysaver.settings

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.SharedPreferences
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_DEFAULT_IS_ENABLED
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_DEFAULT_TIMER
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_ENABLED_KEY
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_TIMER_KEY

class SettingsRepository constructor(private val sharedPreferences: SharedPreferences) {

    fun checkForBluetoothSupport(): Boolean {
        return BluetoothAdapter.getDefaultAdapter() != null
    }

    fun isCheckingBluetooth(): Boolean {
        return sharedPreferences.getBoolean(BLUETOOTH_ENABLED_KEY, BLUETOOTH_DEFAULT_IS_ENABLED)
    }

    @SuppressLint("ApplySharedPref")
    fun enableBluetooth(isEnabled: Boolean): Boolean {
        return sharedPreferences.edit().putBoolean(BLUETOOTH_ENABLED_KEY, isEnabled).commit()
    }

    @SuppressLint("ApplySharedPref")
    fun saveBluetoothTimer(timer: Int): Boolean {
        return sharedPreferences.edit().putInt(BLUETOOTH_TIMER_KEY, timer).commit()
    }

    fun getBluetoothTimer(): Int {
        return sharedPreferences.getInt(BLUETOOTH_TIMER_KEY, BLUETOOTH_DEFAULT_TIMER)
    }
}