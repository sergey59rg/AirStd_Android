package ru.airmasters.airstd_wear

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.clj.fastble.BleManager
import com.clj.fastble.data.BleDevice
import java.util.ArrayList


class DeviceAdapter(private val context: Context) : BaseAdapter() {

    private var bleDeviceList: MutableList<BleDevice>
    private var mListener: OnDeviceClickListener? = null


    init {
        bleDeviceList = ArrayList()
    }

    fun addDevice(bleDevice: BleDevice) {
        removeDevice(bleDevice)
        bleDeviceList.add(bleDevice)
    }

    fun removeDevice(bleDevice: BleDevice) {
        try{
            for (i in bleDeviceList.indices) {
                val device = bleDeviceList[i]
                if (bleDevice.key == device.key) {
                    bleDeviceList.removeAt(i)
                }
            }
        }catch (e: Exception){

        }

    }

    fun clearConnectedDevice() {
        for (i in bleDeviceList.indices) {
            val device = bleDeviceList[i]
            if (BleManager.getInstance().isConnected(device)) {
                bleDeviceList.removeAt(i)
            }
        }
    }

    fun clearScanDevice() {

        bleDeviceList = ArrayList()

        for (i in bleDeviceList.indices) {
            val device = bleDeviceList[i]
            if (!BleManager.getInstance().isConnected(device)) {
                bleDeviceList.removeAt(i)
            }
        }
    }

    fun clear() {
        clearConnectedDevice()
        clearScanDevice()
    }

    override fun getCount(): Int {
        return bleDeviceList.size
    }

    override fun getItem(position: Int): BleDevice? {
        return if (position > bleDeviceList.size) null else bleDeviceList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            convertView = View.inflate(context, R.layout.adapter_device, null)
            holder = ViewHolder()
            convertView!!.tag = holder
            holder.txtname = convertView.findViewById(R.id.txt_name) as TextView
            holder.txtmac = convertView.findViewById(R.id.txt_mac) as TextView
            holder.bluetoothDeviceRelativeLayout = convertView.findViewById(R.id.bluetooth_device_relative_layout) as RelativeLayout
        }

        val bleDevice = getItem(position)
        if (bleDevice != null) {
            val isConnected = BleManager.getInstance().isConnected(bleDevice)
            val name = bleDevice.name
            val mac = bleDevice.mac
            holder.txtname!!.text = name
            holder.txtmac!!.text = mac
        }

        holder.bluetoothDeviceRelativeLayout?.setOnClickListener {
            mListener?.onConnect(bleDevice)
        }


        return convertView
    }

    internal inner class ViewHolder {
        var bluetoothDeviceRelativeLayout: RelativeLayout? = null
        var txtname: TextView? = null
        var txtmac: TextView? = null
    }

    interface OnDeviceClickListener {


        fun onConnect(bleDevice: BleDevice?)

        fun onDisConnect(bleDevice: BleDevice?)

        fun onDetail(bleDevice: BleDevice?)
    }

    fun setOnDeviceClickListener(listener: OnDeviceClickListener) {
        this.mListener = listener
    }

}