package ru.airmasters.airstd_wear.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.clj.fastble.data.BleDevice
import ru.airmasters.airstd_wear.MainActivity
import ru.airmasters.airstd_wear.R
import ru.airmasters.airstd_wear.BleDeviceAdapter


class ServiceListFragment: Fragment() {

    private  lateinit var mainActivity: MainActivity
    private var bleDeviceAdapter: BleDeviceAdapter? = null
    private var connected: Boolean = false
        get() = field
        set(value) {
            field = value
            SetConnectedIndicatorColor(value)
        }

    private lateinit var ConnectedIndicator: ImageButton

    private fun SetConnectedIndicatorColor(value: Boolean){
        ConnectedIndicator.setBackgroundColor( if (value) Color.GREEN else Color.RED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mainActivity = activity as MainActivity
        val v = inflater.inflate(R.layout.fragment_service_list, null)
        initView(v)
        return v
    }

    override fun onResume() {
        super.onResume()
        SetConnectedIndicatorColor(connected)
    }

    private fun write(cmd: ByteArray){
        bleDeviceAdapter?.write(cmd) ?: /*Show Error*/ if(context != null) Toast.makeText(requireContext(), "Проверьте настройки подключения", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(v: View) {

        ConnectedIndicator = v.findViewById(R.id.connected_indicator);

        ConnectedIndicator.setOnClickListener() {
            mainActivity.changePage("ConnectionSettingsFragment")
        }

        v.findViewById<Button>(R.id.connected_indicator_btn).setOnClickListener {
            mainActivity.changePage("ConnectionSettingsFragment")
        }

        val btn_allp = v.findViewById(R.id.cbtn_allp) as Button
        btn_allp.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(1,5,9,13));
        }
        btn_allp.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(0,4,8,12));
            }
            false
        })
        val btn_allm = v.findViewById(R.id.cbtn_allm) as Button
        btn_allm.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(3,7,11,15));
        }
        btn_allm.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(2,6,10,14));
            }
            false
        })

        val btn_frontp = v.findViewById(R.id.cbtn_frontp) as Button
        btn_frontp.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(1, 5));
        }
        btn_frontp.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(0,4));
            }
            false
        })
        val btn_frontm = v.findViewById(R.id.cbtn_frontm) as Button
        btn_frontm.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(3, 7));
        }
        btn_frontm.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(2,6));
            }
            false
        })

        val btn_rearp = v.findViewById(R.id.cbtn_rearp) as Button
        btn_rearp.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(9, 13));
        }
        btn_rearp.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(8,12));
            }
            false
        })
        val btn_rearm = v.findViewById(R.id.cbtn_rearm) as Button
        btn_rearm.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(11, 15));
        }
        btn_rearm.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(10,14));
            }
            false
        })

    }

    fun onConnected(bleDevice: BleDevice){
        connected = true
        reConnectService(bleDevice)
    }

    fun onDisconnected(){
        connected = false
    }

    private fun reConnectService(bleDevice: BleDevice) {
        bleDeviceAdapter = BleDeviceAdapter.construct(bleDevice){msg:String -> if(context != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()}
        if(bleDeviceAdapter == null) return
    }
}