package fr.isen.langeron.androidtoolbox

import android.bluetooth.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bledevice.*



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
                            connectionState.text = "Connecté"
                        }
                        bluetoothGatt?.discoverServices()
                        Log.i(TAG, "Connected to GATT")
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        runOnUiThread {
                            connectionState.text = "Déconnecté"
                        }
                        Log.i(TAG, "Disconnected from GATT")
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    itemView.adapter = BLEServiceAdapter(
                    gatt?.services?.map {BLEService(it.uuid.toString(),it.characteristics)}
                        ?.toMutableList() ?: arrayListOf()
                        , this@BLEDeviceActivity, gatt)
                    itemView.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                }

                }
    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        val value = characteristic.getStringValue(0)
        Log.e(
            "TAG",
            "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString()
        )
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic
    ) {
        val value = characteristic.value
        Log.e(
            "TAG",
            "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString()
        )
    }
}

override fun onStop() {
    super.onStop()
    bluetoothGatt?.close()
}

companion object {
    private const val STATE_DISCONNECTED = "Status : Déconnecté"
    private const val STATE_CONNECTED = "Status : Connecté"
                }
        }

