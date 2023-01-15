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

data class PresetCycle(
    var threshold: Int = 0,
    var upDuration: Int = 0,
    var downDuration: Int = 0,
    var afterDelay: Int = 0
)

class PresetCycleAdapter(private val context: Context) : BaseAdapter() {
    val cycles: MutableList<PresetCycle> = mutableListOf()
    private var btn_psi:TextView? = null
    private var btn_proc:TextView? = null
    private var btn_pop:TextView? = null
    private var bleDeviceAdapter: BleDeviceAdapter? = null
    private  lateinit var mainActivity: MainActivity

    lateinit var edit_threshold: EditText
    lateinit var edit_upDuration: EditText
    lateinit var edit_downDuration: EditText
    lateinit var edit_afterDelay: EditText

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val presetCycle: PresetCycle



        if (convertView == null) {
            convertView = View.inflate(context, R.layout.custtomization_cycle, null)
            presetCycle = PresetCycle()
            convertView!!.tag = presetCycle

            edit_threshold = convertView.findViewById(R.id.edit_threshold)
            edit_upDuration = convertView.findViewById(R.id.edit_upDuration)
            edit_downDuration = convertView.findViewById(R.id.edit_downDuration)
            edit_afterDelay = convertView.findViewById(R.id.edit_afterDelay)

            var threshold = 0
            var upDuration = 0
            var downDuration = 0
            var afterDelay = 0

            edit_threshold.setOnEditorActionListener { textView, i, keyEvent ->
                if (edit_threshold.text.toString().toIntOrNull() ?: -1 != threshold) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    threshold = edit_threshold.text.toString().toIntOrNull() ?: -1
                }
                true
            }
            edit_upDuration.setOnEditorActionListener { textView, i, keyEvent ->
                if (edit_upDuration.text.toString().toIntOrNull() ?: -1 != upDuration) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    upDuration = edit_upDuration.text.toString().toIntOrNull() ?: -1
                }
                true
            }
            edit_downDuration.setOnEditorActionListener { textView, i, keyEvent ->
                if (edit_downDuration.text.toString().toIntOrNull() ?: -1 != downDuration) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    downDuration = edit_downDuration.text.toString().toIntOrNull() ?: -1
                }
                true
            }
            edit_afterDelay.setOnEditorActionListener { textView, i, keyEvent ->
                if (edit_afterDelay.text.toString().toIntOrNull() ?: -1 != afterDelay) {
                    Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                    afterDelay = edit_afterDelay.text.toString().toIntOrNull() ?: -1
                }
                true
            }

        }
        return convertView
    }

    override fun getItem(p0: Int): Any {
        return cycles[p0]
    }

    override fun getItemId(p0: Int): Long {
         return p0.toLong()
    }

    override fun getCount(): Int {
        return cycles.size
    }

    fun setCount(count: Int){
        cycles.clear()
        for(i in 1..count){
            cycles.add(PresetCycle())
        }
    }

}
