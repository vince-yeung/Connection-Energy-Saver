package com.vinceyeung.connectionenergysaver.settings

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vinceyeung.connectionenergysaver.R
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_DEFAULT_TIMER
import timber.log.Timber

class SettingsViewModel @ViewModelInject constructor(
    private val app: Application, private val repository: SettingsRepository
) : ViewModel() {

    private val _isBluetoothSupported = MutableLiveData<Boolean>()
    val isBluetoothSupported: LiveData<Boolean> = _isBluetoothSupported

    private val _bluetoothTimer = MutableLiveData<Int>()

    val bluetoothTimerString: LiveData<String> = Transformations.map(_bluetoothTimer) { progress ->
        Timber.i("Bluetooth Progress: $progress")
        app.resources.getQuantityString(R.plurals.bluetooth_timer_label, progress, progress)
    }

    init {
        checkForBluetoothSupport()
    }

    private fun checkForBluetoothSupport() {
        _isBluetoothSupported.value = repository.checkForBluetoothSupport()
    }

    fun isCheckingBluetooth(): Boolean {
        return repository.isCheckingBluetooth()
    }

    fun enableBluetooth(isEnabled: Boolean) {
        repository.enableBluetooth(isEnabled).also { isSuccessful ->
            if (isSuccessful) {
                val enabledString = when (isEnabled) {
                    true -> app.resources.getString(R.string.common_enabled)
                    false -> app.resources.getString(R.string.common_disabled)
                }

                Timber.i("Bluetooth has been $enabledString")
            } else {
                Timber.e("Error: enableBluetooth")
            }
        }
    }

    fun getBluetoothTimer(): Int {
        _bluetoothTimer.value = repository.getBluetoothTimer()
        return _bluetoothTimer.value ?: BLUETOOTH_DEFAULT_TIMER
    }

    fun setBluetoothTimer(progress: Int) {
        repository.saveBluetoothTimer(progress).also { isSuccessful ->
            if (isSuccessful) {
                _bluetoothTimer.value = progress
            } else {
                Timber.e("Error: setBluetoothProgress")
            }
        }
    }
}