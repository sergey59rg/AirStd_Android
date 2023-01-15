package ru.airmasters.airstd_base
//
//import android.Manifest
//import android.app.AlertDialog
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothGatt
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.PackageManager
//import android.os.Build
//import android.provider.Settings
//import android.view.View
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.clj.fastble.BleManager
//import com.clj.fastble.callback.BleGattCallback
//import com.clj.fastble.callback.BleScanCallback
//import com.clj.fastble.data.BleDevice
//import com.clj.fastble.exception.BleException
//import com.desarollobluetooth.fragments.*
//import ru.airmasters.airstd.observers.ObserverManager
//import java.util.ArrayList
//
//class ConnectionControlService(val pref: SharedPreferences) {
//
//    private fun checkPermissions() {
//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (!bluetoothAdapter.isEnabled) {
//            Toast.makeText(activity, getString(R.string.please_open_blue), Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val permissions = arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        val permissionDeniedList = ArrayList<String>()
//        for (permission in permissions) {
//            val permissionCheck = context?.let { ContextCompat.checkSelfPermission(it, permission) }
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                onPermissionGranted(permission)
//            } else {
//                permissionDeniedList.add(permission)
//            }
//        }
//        if (!permissionDeniedList.isEmpty()) {
//            val deniedPermissions = permissionDeniedList.toTypedArray()
//            activity?.let {
//                ActivityCompat.requestPermissions(
//                    it,
//                    deniedPermissions,
//                    REQUEST_CODE_PERMISSION_LOCATION
//                )
//            }
//        }
//    }
//
//    private fun onPermissionGranted(permission: String) {
//        when (permission) {
//            Manifest.permission.ACCESS_FINE_LOCATION ->
//                if (Build.VERSION.SDK_INT >=
//                    Build.VERSION_CODES.M && !checkGPSIsOpen()
//                ) {
//                    AlertDialog.Builder(context)
//                        .setTitle(R.string.notifyTitle)
//                        .setMessage(R.string.gpsNotifyMsg)
//                        .setNegativeButton(R.string.cancel,
//                            DialogInterface.OnClickListener { dialog, which -> activity?.finish() })
//                        .setPositiveButton(R.string.setting,
//                            DialogInterface.OnClickListener { dialog, which ->
//                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                                startActivityForResult(intent, REQUEST_CODE_OPEN_GPS)
//                            })
//
//                        .setCancelable(false)
//                        .show()
//                } else {
//                    startScan()
//                }
//        }
//    }
//
//
//    private fun startScan() {
//        BleManager.getInstance().scan(object : BleScanCallback() {
//            override fun onScanStarted(success: Boolean) {
//                mDeviceAdapter?.clearScanDevice()
//                mDeviceAdapter?.notifyDataSetChanged()
//                img_loading?.startAnimation(operatingAnim)
//                img_loading?.visibility = View.VISIBLE
//                btn_scan?.text = getString(R.string.stop_scan)
//            }
//
//            override fun onLeScan(bleDevice: BleDevice?) {
//                super.onLeScan(bleDevice)
//            }
//
//            override fun onScanning(bleDevice: BleDevice) {
//                if(bleDevice.name != null && bleDevice.name != "") {
//                    mDeviceAdapter?.addDevice(bleDevice)
//                    mDeviceAdapter?.notifyDataSetChanged()
////                    if(pref.getString("DeviceName", "") == bleDevice.name){
////                        connect(bleDevice)
////                    }
//                }
//            }
//
//            override fun onScanFinished(scanResultList: List<BleDevice>) {
//                img_loading?.clearAnimation()
//                img_loading?.setVisibility(View.INVISIBLE)
//                btn_scan?.setText(getString(R.string.start_scan))
//            }
//        })
//    }
//
//    private fun connect(bleDevice: BleDevice?) {
//        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
//            override fun onStartConnect() {
//                progressDialog?.show()
//            }
//
//            override fun onConnectFail(bleDevice: BleDevice, exception: BleException) {
//                img_loading?.clearAnimation()
//                img_loading?.setVisibility(View.INVISIBLE)
//                btn_scan?.setText(getString(R.string.start_scan))
//                progressDialog?.dismiss()
//                Toast.makeText(
//                    context,
//                    getString(R.string.connect_fail),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
//                progressDialog?.dismiss()
//                mDeviceAdapter?.addDevice(bleDevice)
//                mDeviceAdapter?.notifyDataSetChanged()
//                onButtonPressed(bleDevice)
//
//                mDeviceAdapter?.clearScanDevice()
//                mDeviceAdapter?.notifyDataSetChanged()
//
//                pref.edit().putString("DeviceName", bleDevice.name).apply()
//            }
//
//            override fun onDisConnected(
//                isActiveDisConnected: Boolean,
//                bleDevice: BleDevice,
//                gatt: BluetoothGatt,
//                status: Int
//            ) {
//                progressDialog?.dismiss()
//                btns_?.visibility = View.INVISIBLE
//
//                mDeviceAdapter?.removeDevice(bleDevice)
//                mDeviceAdapter?.notifyDataSetChanged()
//
//                if (isActiveDisConnected) {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.active_disconnected),
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.disconnected),
//                        Toast.LENGTH_LONG
//                    ).show()
//                    ObserverManager.getInstance().notifyObserver(bleDevice)
//                }
//
//            }
//        })
//    }
//}