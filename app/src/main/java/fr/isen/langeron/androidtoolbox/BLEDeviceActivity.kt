package fr.isen.langeron.androidtoolbox

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bledevice.*
import kotlinx.android.synthetic.main.activity_bledevice_adapter.*


class BLEDeviceActivity : AppCompatActivity() {



        private var bluetoothGatt: BluetoothGatt? = null
        private var TAG: String = "services"


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_bledevice)

            val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
            bluetoothGatt = device.connectGatt(this, true, gattCallback)

        }

        private val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        runOnUiThread {
                            textUUID.text = "Connecté"
                        }
                        bluetoothGatt?.discoverServices()
                        Log.i(TAG, "Connected to GATT")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        runOnUiThread {
                            textUUID.text = "Déconnecté"
                        }
                        Log.i(TAG, "Disconnected from GATT")
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    bleServiceList.adapter = BLEServiceAdapter(
                    gatt?.services?.map {BLEService(it.uuid.toString(),it.characteristics)}
                        ?.toMutableList() ?: arrayListOf()
                    )
                    bleServiceList.LayoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                }

                }
            }
        }

