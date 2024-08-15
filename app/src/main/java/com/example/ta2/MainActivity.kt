@file:Suppress("DEPRECATION")

package com.example.ta2

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val requestEnableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Bluetooth has been turned on
                Toast.makeText(this, "Bluetooth turned ON", Toast.LENGTH_SHORT).show()
            } else {
                // User canceled or Bluetooth activation failed
                Toast.makeText(
                    this,
                    "Bluetooth activation canceled or failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize BluetoothAdapter using BluetoothManager
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Button click listeners
        val onButton: Button = findViewById(R.id.button)
        val offButton: Button = findViewById(R.id.button2)
        val deviceButton: Button = findViewById(R.id.button3)

        onButton.setOnClickListener { onButtonClicked() }
        offButton.setOnClickListener { offButtonClicked() }
        deviceButton.setOnClickListener { deviceButtonClicked() }

        // Check and request Bluetooth permission if needed
        checkBluetoothPermission()
    }

    private fun checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }

    private fun onButtonClicked() {
        // Check Bluetooth permission before trying to enable Bluetooth
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!bluetoothAdapter.isEnabled) {
                // Launch Bluetooth enable request
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestEnableBluetoothLauncher.launch(enableBtIntent)
            } else {
                Toast.makeText(this, "Bluetooth is already ON", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun offButtonClicked() =
        // Check Bluetooth permission before trying to disable Bluetooth
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
                Toast.makeText(this, "Bluetooth turned OFF", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth is already OFF", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }

    private fun deviceButtonClicked() {
        // Handle device button click
        Toast.makeText(this, "Device button clicked", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_BLUETOOTH_PERMISSION = 1
    }
}
