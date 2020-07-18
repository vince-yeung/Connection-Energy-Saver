package com.vinceyeung.connectionenergysaver.settings

import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vinceyeung.connectionenergysaver.R
import com.vinceyeung.connectionenergysaver.databinding.FragmentSettingsBinding
import com.vinceyeung.connectionenergysaver.utilities.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bluetoothEnableSwitch.isChecked = viewModel.isCheckingBluetooth()
        binding.bluetoothTimerSeekBar.progress = viewModel.getBluetoothTimer()

        binding.bluetoothEnableSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.enableBluetooth(isChecked)
        }

        binding.bluetoothTimerSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> showAlertDialog(
                requireContext(), R.string.info_dialog_title, R.string.info_dialog_message
            ).show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun disableBluetoothSupport() {
        binding.bluetoothEnableSwitch.isEnabled = false
        binding.bluetoothTimerSeekBar.isEnabled = false

        Toast.makeText(context, R.string.error_no_bluetooth_support, Toast.LENGTH_LONG).show()
    }
}