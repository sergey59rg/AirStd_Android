package ru.airmasters.airstd_mini.fragment

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
import ru.airmasters.airstd_mini.MainActivity
import ru.airmasters.airstd_mini.R
import ru.airmasters.airstd_base.BleDeviceAdapter


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
        bleDeviceAdapter?.write(cmd) ?: /*Show Error*/ if(context != null) Toast.makeText(context!!, "Проверьте настройки подключения", Toast.LENGTH_LONG).show()
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

        val btn_0 = v.findViewById(R.id.cbtn_0) as Button
        btn_0.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(1));
        }
        btn_0.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(0));
            }
            false
        })
        val btn_1 = v.findViewById(R.id.cbtn_1) as Button
        btn_1.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(3));
        }
        btn_1.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(2));
            }
            false
        })
        val btn_2 = v.findViewById(R.id.cbtn_2) as Button
        btn_2.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(5));
        }
        btn_2.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(4));
            }
            false
        })
        val btn_3 = v.findViewById(R.id.cbtn_3) as Button
        btn_3.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(7));
        }
        btn_3.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(6));
            }
            false
        })
        val btn_4 = v.findViewById(R.id.cbtn_4) as Button
        btn_4.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(9));
        }
        btn_4.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(8));
            }
            false
        })
        val btn_5 = v.findViewById(R.id.cbtn_5) as Button
        btn_5.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(11));
        }
        btn_5.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(10));
            }
            false
        })
        val btn_6 = v.findViewById(R.id.cbtn_6) as Button
        btn_6.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(13));
        }
        btn_6.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(12));
            }
            false
        })
        val btn_7 = v.findViewById(R.id.cbtn_7) as Button
        btn_7.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(15));
        }
        btn_7.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(14));
            }
            false
        })
        val btn_02 = v.findViewById(R.id.cbtn_02) as Button
        btn_02.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(1, 5));
        }
        btn_02.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(0,4));
            }
            false
        })
        val btn_13 = v.findViewById(R.id.cbtn_13) as Button
        btn_13.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(3, 7));
        }
        btn_13.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(2,6));
            }
            false
        })
        val btn_46 = v.findViewById(R.id.cbtn_46) as Button
        btn_46.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(9, 13));
        }
        btn_46.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(8,12));
            }
            false
        })
        val btn_57 = v.findViewById(R.id.cbtn_57) as Button
        btn_57.setOnClickListener() {// Button Unpressed
            write(byteArrayOf(11, 15));
        }
        btn_57.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                write(byteArrayOf(10,14));
            }
            false
        })
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
    }

    fun onConnected(bleDevice: BleDevice){
        connected = true
        reConnectService(bleDevice)
    }

    fun onDisconnected(){
        connected = false
    }

    private fun reConnectService(bleDevice: BleDevice) {
        bleDeviceAdapter = BleDeviceAdapter.construct(bleDevice){msg:String -> if(context != null) Toast.makeText(context!!, msg, Toast.LENGTH_LONG).show()}
        if(bleDeviceAdapter == null) return
    }
}