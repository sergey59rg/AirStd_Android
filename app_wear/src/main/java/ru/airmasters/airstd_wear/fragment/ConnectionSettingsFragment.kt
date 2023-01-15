package ru.airmasters.airstd_wear.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.data.BleScanState
import com.clj.fastble.exception.BleException
import pl.droidsonroids.gif.GifImageView
import ru.airmasters.airstd_wear.DeviceAdapter
import ru.airmasters.airstd_wear.R
import ru.airmasters.airstd_wear.observers.ObserverManager
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_CODE_OPEN_GPS = 1
private const val REQUEST_CODE_PERMISSION_LOCATION = 2
private var mDeviceAdapter: DeviceAdapter? = null
private var layout_setting: LinearLayout? = null
private var btns_: LinearLayout? = null
private var btn_scan: Button? = null
private var btn_write: Button? = null
private var btn_notify: Button? = null
private var img_loading: GifImageView? = null

private var up_txt: TextView? = null
private var beak_txt: TextView? = null

private var operatingAnim: Animation? = null
private var progressDialog: ProgressDialog? = null
private var listener: ConnectionSettingsFragment.OnFragmentInteractionListener? = null



@SuppressLint("ValidFragment")
class ConnectionSettingsFragment(val pref: SharedPreferences) : Fragment(), DeviceAdapter.OnDeviceClickListener {

    override fun onConnect(bleDevice: BleDevice?) {
        if (!BleManager.getInstance().isConnected(bleDevice)) {
            BleManager.getInstance().cancelScan()
            connect(bleDevice)
        }
    }

    override fun onDisConnect(bleDevice: BleDevice?) {
        if (BleManager.getInstance().isConnected(bleDevice)) {
            btns_?.visibility = View.INVISIBLE
            BleManager.getInstance().disconnect(bleDevice)
            listener?.onDisconnected(bleDevice)
        }
    }

    override fun onDetail(bleDevice: BleDevice?) {
        if (BleManager.getInstance().isConnected(bleDevice)) {
            onButtonPressed(bleDevice)
        }
    }

    init{
        BleManager.getInstance()
            .enableLog(true)
            .setReConnectCount(10, 1000)
            .setConnectOverTime(20000).operateTimeout = 1000
    }

    fun init(){
//        startScan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_connection_settings,
            null
        )

        up_txt = view.findViewById(R.id.up_txt) as TextView
        beak_txt = view.findViewById(R.id.beak_txt) as TextView
        beak_txt?.setOnClickListener {
            if (beak_txt?.text == "Disconnect end start scanner") {
                val bleDevice = listener?.getBleDevice()
                if (bleDevice != null) {
                    onDisConnect(bleDevice)
                    pref.edit().remove("DeviceName").apply()
                    checkPermissions()
                }
            }
        }


        layout_setting?.visibility = View.GONE

        img_loading = view.findViewById(R.id.img_loading) as GifImageView
        progressDialog = ProgressDialog(context, 3)
        if(mDeviceAdapter == null) {
            mDeviceAdapter = DeviceAdapter(requireContext())
            mDeviceAdapter?.setOnDeviceClickListener(this)
        }

        val bleDevice = listener?.getBleDevice()
        if (BleManager.getInstance().isConnected(bleDevice)) {
            textConnected()
        }
        btn_notify?.setOnClickListener {
            //  onClickNotify()
        }

        btn_write?.setOnClickListener {
            //  onClickWrite()
        }


        val listViewDevice = view.findViewById(R.id.list_device) as ListView
        listViewDevice.adapter = mDeviceAdapter

        return view
    }

    private fun textSearch(){
        img_loading?.visibility = View.VISIBLE
        up_txt?.text = "Searching for controller"
        beak_txt?.text="Power on your car"
        if(context != null) {
            beak_txt?.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryText))
        }
    }
    private fun textFound(){
        img_loading?.visibility = View.INVISIBLE
        up_txt?.text = "Found controllers"
        beak_txt?.text="Choose one to connect"
        if(context != null) {
            beak_txt?.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryText))
        }
    }
    private fun textConnected(){
        img_loading?.visibility = View.INVISIBLE
        up_txt?.text="Connected controller"
        beak_txt?.text="Disconnect end start scanner"
        if(context != null) {
            beak_txt?.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorSecondaryText))
        }
    }


    fun onButtonPressed(BleDvice: BleDevice?) {
        listener?.onConnected(BleDvice)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
            val bleDevice = listener?.getBleDevice()

            if (!BleManager.getInstance().isConnected(bleDevice)) {
                checkPermissions()
            }
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
//        listener = null
    }


    private fun checkPermissions() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(activity, getString(R.string.please_open_blue), Toast.LENGTH_LONG).show()
            return
        }

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionDeniedList = ArrayList<String>()
        for (permission in permissions) {
            val permissionCheck = context?.let { ContextCompat.checkSelfPermission(it, permission) }
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    deniedPermissions,
                    REQUEST_CODE_PERMISSION_LOCATION
                )
            }
        }
    }

    private fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION ->
                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M && !checkGPSIsOpen()
                ) {
                    AlertDialog.Builder(context)
                        .setTitle(R.string.notifyTitle)
                        .setMessage(R.string.gpsNotifyMsg)
                        .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, which -> activity?.finish() })
                        .setPositiveButton(R.string.setting,
                            DialogInterface.OnClickListener { dialog, which ->
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivityForResult(intent, REQUEST_CODE_OPEN_GPS)
                            })

                        .setCancelable(false)
                        .show()
                } else {
                    startScan()
                }
        }
    }

    private fun showConnectedDevice() {
        val deviceList = BleManager.getInstance().allConnectedDevice
        mDeviceAdapter?.clearConnectedDevice()
        for (bleDevice in deviceList) {
            mDeviceAdapter?.addDevice(bleDevice)
        }
        mDeviceAdapter?.notifyDataSetChanged()
    }


    private fun startScan() {
        if(BleManager.getInstance().scanSate == BleScanState.STATE_SCANNING){
            BleManager.getInstance().cancelScan()
        }
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                mDeviceAdapter?.clearScanDevice()
                mDeviceAdapter?.notifyDataSetChanged()
                textSearch()
                btn_scan?.text = getString(R.string.stop_scan)
            }

            override fun onLeScan(bleDevice: BleDevice?) {
                super.onLeScan(bleDevice)
            }

            override fun onScanning(bleDevice: BleDevice) {
                if(bleDevice.name != null && bleDevice.name != "") {
                    mDeviceAdapter?.addDevice(bleDevice)
                    mDeviceAdapter?.notifyDataSetChanged()
                    if(pref.getString("DeviceName", "") == bleDevice.name){
                        onConnect(bleDevice)
                    }
                }
            }

            override fun onScanFinished(scanResultList: List<BleDevice>) {
                textFound()

//                img_loading?.clearAnimation()
//                img_loading?.setVisibility(View.INVISIBLE)
//                btn_scan?.setText(getString(R.string.start_scan))
            }
        })
    }

    private fun connect(bleDevice: BleDevice?) {
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
            override fun onStartConnect() {
                progressDialog?.show()
            }

            override fun onConnectFail(bleDevice: BleDevice, exception: BleException) {
                img_loading?.clearAnimation()
                img_loading?.setVisibility(View.INVISIBLE)
                btn_scan?.setText(getString(R.string.start_scan))
                progressDialog?.dismiss()
                Toast.makeText(
                    context,
                    getString(R.string.connect_fail),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
                progressDialog?.dismiss()

                mDeviceAdapter?.clearScanDevice()
                mDeviceAdapter?.notifyDataSetChanged()

                mDeviceAdapter?.addDevice(bleDevice)
                mDeviceAdapter?.notifyDataSetChanged()
                onButtonPressed(bleDevice)

                pref.edit().putString("DeviceName", bleDevice.name).apply()

                textConnected()
                
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                bleDevice: BleDevice,
                gatt: BluetoothGatt,
                status: Int
            ) {
                progressDialog?.dismiss()
                btns_?.visibility = View.INVISIBLE

                mDeviceAdapter?.removeDevice(bleDevice)
                mDeviceAdapter?.notifyDataSetChanged()

                if (!isActiveDisConnected) {
                    ObserverManager.getInstance().notifyObserver(bleDevice)
                }
                listener
            }
        })
    }


    private fun checkGPSIsOpen(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager ?: return false
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    interface OnFragmentInteractionListener {
        fun onConnected(bledevice: BleDevice?)
        fun onDisconnected(bledevice: BleDevice?)
        fun getBleDevice(): BleDevice?
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, prefs: SharedPreferences) =
            ConnectionSettingsFragment(prefs).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}