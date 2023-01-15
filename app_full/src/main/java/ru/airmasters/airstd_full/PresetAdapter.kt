package ru.airmasters.airstd_full

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.View.OnTouchListener
import android.widget.*
import com.clj.fastble.BleManager
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import ru.airmasters.airstd_base.BleDeviceAdapter
import ru.airmasters.airstd_full.R

data class Preset(
    var FL: Int = 0,
    var FR: Int = 0,
    var RL: Int = 0,
    var RR: Int = 0
)

class PresetAdapter(private val context: Context) : BaseAdapter() {
    val preset: MutableList<Preset> = mutableListOf()
    private var btn_psi:TextView? = null
    private var btn_proc:TextView? = null
    private var btn_pop:TextView? = null
    private var bleDeviceAdapter: BleDeviceAdapter? = null
    private  lateinit var mainActivity: MainActivity


    lateinit var FR: EditText
    lateinit var FL: EditText
    lateinit var RL: EditText
    lateinit var RR: EditText

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val preset: Preset



        if (convertView == null) {
            convertView = View.inflate(context, R.layout.custtomization_preset, null)
            preset = Preset()
            convertView!!.tag = preset

            FL = convertView.findViewById(R.id.FL)
            FR = convertView.findViewById(R.id.FR)
            RL = convertView.findViewById(R.id.RL)
            RR = convertView.findViewById(R.id.RR)


            var FrontLeft = 10
            var FrontRight = 10
            var RearLeft = 10
            var RearRight = 10

            FL.setOnEditorActionListener { textView, i, keyEvent ->
                if (FL.text.toString().toIntOrNull() ?: -1 != FrontLeft) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    FrontLeft = FL.text.toString().toIntOrNull() ?: -1
                    // position - Это номер пресета
                }
                true
            }
            FR.setOnEditorActionListener { textView, i, keyEvent ->
                if (FR.text.toString().toIntOrNull() ?: -1 != FrontRight) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    FrontRight = FR.text.toString().toIntOrNull() ?: -1
                }
                true
            }
            RL.setOnEditorActionListener { textView, i, keyEvent ->
                if (RL.text.toString().toIntOrNull() ?: -1 != RearLeft) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    RearLeft = RL.text.toString().toIntOrNull() ?: -1
                }
                true
            }
            RR.setOnEditorActionListener { textView, i, keyEvent ->
                if (RR.text.toString().toIntOrNull() ?: -1 != RearRight) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    RearRight = RR.text.toString().toIntOrNull() ?: -1
                }
                true
            }
        }
        return convertView
    }

    override fun getItem(p0: Int): Any {
        return preset[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return preset.size
    }

    fun setCount(count: Int){
        preset.clear()
        for(i in 1..count){
            preset.add(Preset())
        }
    }
}
