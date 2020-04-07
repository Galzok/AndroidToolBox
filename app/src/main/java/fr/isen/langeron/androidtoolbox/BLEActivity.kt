package fr.isen.langeron.androidtoolbox

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ble.*
import kotlin.collections.ArrayList


class BLEActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BluetoothActivityAdapter
    private val devices = ArrayList<ScanResult>()

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble)

        bleTextFail.visibility = View.GONE

        imageScan.setOnClickListener {
            when {
                isBLEEnabled -> {
                    //init scan
                    if (textView9.text == "Lancer le Scan BLE") {
                       imageScan.setImageResource(android.R.drawable.ic_media_pause)
                        textView9.text = "Scan en cours ..."
                    initBLEScan()
                    initScan()
                } else if (textView9.text == "Scan en cours ...") {
                imageScan.setImageResource(android.R.drawable.ic_media_play)
                textView9.text = "Lancer le Scan BLE"
                progressBar.visibility = View.INVISIBLE
                bleDivider.visibility = View.VISIBLE
            }
            }
                bluetoothAdapter != null -> {
                    //ask for permission
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
                }
                else -> {
                    //device is not compatible with your device
                    bleTextFail.visibility = View.VISIBLE
                }
            }
        }
        deviceRecyclerView.adapter = BluetoothActivityAdapter(devices, ::onDeviceClicked)
        deviceRecyclerView.layoutManager = LinearLayoutManager(this)
    }



    private fun initScan() {
        progressBar.visibility = View.VISIBLE
        bleDivider.visibility = View.GONE

        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                Log.w("BLEScanActivity", "Scanning for devices")
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
                adapter.clearResults()
                adapter.notifyDataSetChanged()
                progressBar.progress = (progressBar.progress + 5) % 100
            } else {
                mScanning = false
                stopScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLE", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initBLEScan() {
        adapter = BluetoothActivityAdapter(
            arrayListOf(),
            ::onDeviceClicked
        )
        deviceRecyclerView.adapter = adapter
        deviceRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        handler = Handler()

        scanLeDevice(true)
        deviceRecyclerView.setOnClickListener{
            scanLeDevice(!mScanning)
        }

    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BLEDeviceActivity::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnabled) {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    companion object {
        private const val SCAN_PERIOD: Long = 10000
        private const val REQUEST_ENABLE_BT = 44
    }
}


