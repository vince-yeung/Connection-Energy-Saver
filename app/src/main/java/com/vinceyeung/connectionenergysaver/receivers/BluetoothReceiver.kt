package com.vinceyeung.connectionenergysaver.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vinceyeung.connectionenergysaver.di.RepositoryModule
import com.vinceyeung.connectionenergysaver.settings.SettingsRepository
import com.vinceyeung.connectionenergysaver.utilities.BLUETOOTH_ALARM_ID
import com.vinceyeung.connectionenergysaver.utilities.DISABLE_BLUETOOTH_COMMAND
import com.vinceyeung.connectionenergysaver.utilities.PREFERENCE_FILE_NAME
import java.util.*
import java.util.concurrent.TimeUnit

class BluetoothReceiver : BroadcastReceiver() {

    private lateinit var repository: SettingsRepository

    override fun onReceive(context: Context, intent: Intent) {
        repository = RepositoryModule.provideSettingsRepository(
            context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        )

        if (repository.isCheckingBluetooth()) {
            when (intent.action) {
                Intent.ACTION_BOOT_COMPLETED -> {
                    if (!isBluetoothConnected()) {
                        startBluetoothJob(context)
                    }
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> startBluetoothJob(context)
                BluetoothDevice.ACTION_ACL_CONNECTED -> cancelBluetoothJob(context)
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                        BluetoothAdapter.STATE_ON -> startBluetoothJob(context)
                    }
                }
                DISABLE_BLUETOOTH_COMMAND -> disableBluetooth()
            }
        }
    }

    private fun isBluetoothConnected(): Boolean {
        BluetoothAdapter.getDefaultAdapter()?.let { adapter ->
            return adapter.isEnabled && adapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED
        }

        return false
    }

    private fun startBluetoothJob(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getBluetoothPendingIntent(context)

        val delay = TimeUnit.MINUTES.toMillis(repository.getBluetoothTimer().toLong())

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + delay, pendingIntent
        )
    }

    private fun cancelBluetoothJob(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getBluetoothPendingIntent(context)

        alarmManager.cancel(pendingIntent)
    }

    private fun getBluetoothPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, BluetoothReceiver::class.java)
        intent.action = DISABLE_BLUETOOTH_COMMAND

        return PendingIntent.getBroadcast(context, BLUETOOTH_ALARM_ID, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun disableBluetooth() {
        BluetoothAdapter.getDefaultAdapter()?.disable()
    }
}