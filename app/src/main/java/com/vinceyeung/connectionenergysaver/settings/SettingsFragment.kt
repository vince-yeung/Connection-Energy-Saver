package com.vinceyeung.connectionenergysaver.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vinceyeung.connectionenergysaver.R
import com.vinceyeung.connectionenergysaver.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bluetoothEnableSwitch.isChecked = viewModel.isCheckingBluetooth()
        binding.bluetoothTimerSeekBar.progress = viewModel.getBluetoothTimer()

        binding.bluetoothEnableSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.enableBluetooth(isChecked)
        }

        binding.bluetoothTimerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    viewModel.setBluetoothTimer(it.progress + 1)
                }
            }
        })

        viewModel.isBluetoothSupported.observe(viewLifecycleOwner, Observer {
            it?.let { isSupported ->
                if (!isSupported) {
                    disableBluetoothSupport()
                }
            }
        })
    }

    private fun disableBluetoothSupport() {
        binding.bluetoothEnableSwitch.isEnabled = false
        binding.bluetoothTimerSeekBar.isEnabled = false

        Toast.makeText(context, R.string.error_no_bluetooth_support, Toast.LENGTH_LONG).show()
    }
}