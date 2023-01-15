package ru.airmasters.airstd_full.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.clj.fastble.data.BleDevice
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import ru.airmasters.airstd_full.MainActivity
import ru.airmasters.airstd_full.R
import ru.airmasters.airstd_base.BleDeviceAdapter
import java.util.*
import kotlin.concurrent.schedule


class ServiceListFragment: Fragment() {
    private var TextViewFrontLeftValue:TextView? = null
    private var TextViewFrontRightValue:TextView? = null
    private var TextViewRearRightValue:TextView? = null
    private var TextViewCompressorPressure:TextView? = null

    private var TextViewRearLeftValue:TextView? = null

    private val timer: Timer

    init {
        timer = Timer("Timer", true)
    }


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
        TextViewFrontLeftValue = v.findViewById(R.id.TextViewFrontLeftValue) as TextView
        TextViewFrontRightValue = v.findViewById(R.id.TextViewFrontRightValue) as TextView
        TextViewRearRightValue = v.findViewById(R.id. TextViewRearRightValue) as TextView
        TextViewCompressorPressure = v.findViewById(R.id.TextViewCompressorPressure) as TextView
        TextViewRearLeftValue = v.findViewById(R.id.TextViewRearLeftValue) as TextView

        ConnectedIndicator = v.findViewById(R.id.connected_indicator);

        ConnectedIndicator.setOnClickListener() {
            mainActivity.changePage("ConnectionSettingsFragment")
        }

        v.findViewById<Button>(R.id.connected_indicator_btn).setOnClickListener {
            mainActivity.changePage("ConnectionSettingsFragment")
        }

        v.findViewById<Button>(R.id.customization_btn).setOnClickListener {
            mainActivity.changePage("CustomizationFragment")
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
        val btn_Preset0 = v.findViewById(R.id.cbtn_p1) as Button
        btn_Preset0.setOnClickListener() {
            write(byteArrayOf(65,0))
        }
        btn_Preset0.setOnLongClickListener() {
            write(byteArrayOf(66,0))
            false
        }
        val btn_Preset1 = v.findViewById(R.id.cbtn_p2) as Button
        btn_Preset1.setOnClickListener() {
            write(byteArrayOf(65,1))
        }
        btn_Preset1.setOnLongClickListener() {
            write(byteArrayOf(66,1))
            false
        }
        val btn_Preset2 = v.findViewById(R.id.cbtn_p3) as Button
        btn_Preset2.setOnClickListener() {
            write(byteArrayOf(65,2))
        }
        btn_Preset2.setOnLongClickListener() {
            write(byteArrayOf(66,2))
            false
        }
        val btn_Preset3 = v.findViewById(R.id.cbtn_p4) as Button
        btn_Preset3.setOnClickListener() {
            write(byteArrayOf(65,3))
        }
        btn_Preset3.setOnLongClickListener() {
            write(byteArrayOf(66,3))
            false
        }
//        val btn_02 = v.findViewById(R.id.cbtn_02) as Button
//        btn_02.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(1, 5));
//        }
//        btn_02.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(0,4));
//            }
//            false
//        })
//        val btn_13 = v.findViewById(R.id.cbtn_13) as Button
//        btn_13.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(3, 7));
//        }
//        btn_13.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(2,6));
//            }
//            false
//        })
//        val btn_46 = v.findViewById(R.id.cbtn_46) as Button
//        btn_46.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(9, 13));
//        }
//        btn_46.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(8,12));
//            }
//            false
//        })
//        val btn_57 = v.findViewById(R.id.cbtn_57) as Button
//        btn_57.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(11, 15));
//        }
//        btn_57.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(10,14));
//            }
//            false
//       })
//        val btn_allp = v.findViewById(R.id.cbtn_allp) as Button
//        btn_allp.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(1,5,9,13));
//        }
//        btn_allp.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(0,4,8,12));
//            }
//            false
//        })
//        val btn_allm = v.findViewById(R.id.cbtn_allm) as Button
//        btn_allm.setOnClickListener() {// Button Unpressed
//            write(byteArrayOf(3,7,11,15));
//        }
//        btn_allm.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                write(byteArrayOf(2,6,10,14));
//            }
//            false
//        })
        timer.schedule(0,1000) {
            if(connected)
                write(byteArrayOf(67,0))
        }
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
        bleDeviceAdapter?.openNotify {msg ->
            try {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                var jsonData: Map<String, Any> =
                    gson.fromJson(msg, object : TypeToken<Map<String, Any>>() {}.type)
                var a = 0;
                a++;
                val AllCarInformation: LinkedTreeMap<String, Any> =
                    jsonData["AllCarInformation"] as LinkedTreeMap<String, Any>
                val CompressorPressure: LinkedTreeMap<String, Any> =
                    AllCarInformation["CompressorPressure"] as LinkedTreeMap<String, Any>
                CompressorPressure["value"];
                val WheelPositions: LinkedTreeMap<String, Any> =
                    AllCarInformation["WheelPositions"] as LinkedTreeMap<String, Any>
                val FrontLeft: LinkedTreeMap<String, Any> =
                    WheelPositions["FrontLeft"] as LinkedTreeMap<String, Any>
                val FrontRight: LinkedTreeMap<String, Any> =
                    WheelPositions["FrontRight"] as LinkedTreeMap<String, Any>
                val RearLeft: LinkedTreeMap<String, Any> =
                    WheelPositions["RearLeft"] as LinkedTreeMap<String, Any>
                val RearRight: LinkedTreeMap<String, Any> =
                    WheelPositions["RearRight"] as LinkedTreeMap<String, Any>

                TextViewFrontLeftValue?.text = FrontLeft["value"].toString()
                TextViewFrontRightValue?.text = FrontRight["value"].toString()
                TextViewRearLeftValue?.text = RearLeft["value"].toString()
                TextViewRearRightValue?.text = RearRight["value"].toString()
                TextViewCompressorPressure?.text = CompressorPressure["value"].toString()
            }
            catch (e:Exception){}

        }
    }
}