package ru.airmasters.airstd_full.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.view.View.OnTouchListener
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.custtomization_cycle.*
import kotlinx.android.synthetic.main.custtomization_preset.*
import kotlinx.android.synthetic.main.fragment_customization.*
import ru.airmasters.airstd_base.BleDeviceAdapter
import ru.airmasters.airstd_full.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.toString as toString1


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var layout_setting: LinearLayout? = null


    @SuppressLint("ValidFragment")
class CustomizationFragment(val pref: SharedPreferences) : Fragment() {

    lateinit var presetCycleAdapter: PresetCycleAdapter
    lateinit var presetAdapter: PresetAdapter
    lateinit var kolCyc: EditText
    lateinit var editupThreshold : EditText
    lateinit var editdownThreshold: EditText
    lateinit var listView: ListView
    lateinit var listPreset: ListView

    lateinit var allminValue: TextView
    lateinit var alldivider: TextView
    lateinit var allmaxValue: TextView

    lateinit var FLminValue: TextView
    lateinit var FLdivider: TextView
    lateinit var FLmaxValue: TextView

    lateinit var FRminValue: TextView
    lateinit var FRdivider: TextView
    lateinit var FRmaxValue: TextView

    lateinit var RLminValue: TextView
    lateinit var RLdivider: TextView
    lateinit var RLmaxValue: TextView

    lateinit var RRminValue: TextView
    lateinit var RRdivider: TextView
    lateinit var RRmaxValue: TextView

    lateinit var kolPre: EditText

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

    private fun SetConnectedIndicatorColor(value: Boolean){
        ConnectedIndicator.setBackgroundColor( if (value) Color.GREEN else Color.RED)
    }

    private lateinit var ConnectedIndicator: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_customization,
            null
        )
        layout_setting?.visibility = View.GONE

        initView(view)

        return view
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initView(v: View) {


        var oldkolCyc = 0
        var upThreshold = 0
        var downThreshold = 0

        var minValue = 0
        var maxValue = 0
        var divider = 0
        var marB = 0
        var oldkolPre = 0
        var number = 0
        var type = " "

        editupThreshold = v.findViewById(R.id.editText10)
        kolCyc = v.findViewById(R.id.editText1)
        editdownThreshold = v.findViewById(R.id.editText20)
        listView = v.findViewById(R.id.listView)
        listPreset = v.findViewById(R.id.listPreset)

        allminValue = v.findViewById(R.id.editText03)
        alldivider = v.findViewById(R.id.editText02)
        allmaxValue = v.findViewById(R.id.editText01)

        FLminValue = v.findViewById(R.id.editText06)
        FLdivider = v.findViewById(R.id.editText05)
        FLmaxValue = v.findViewById(R.id.editText04)

        FRminValue = v.findViewById(R.id.editText09)
        FRdivider = v.findViewById(R.id.editText08)
        FRmaxValue = v.findViewById(R.id.editText07)

        RLminValue = v.findViewById(R.id.editText012)
        RLdivider = v.findViewById(R.id.editText011)
        RLmaxValue = v.findViewById(R.id.editText010)

        RRminValue = v.findViewById(R.id.editText015)
        RRdivider = v.findViewById(R.id.editText014)
        RRmaxValue = v.findViewById(R.id.editTex013)

        kolPre = v.findViewById(R.id.editTextPreset)


        presetCycleAdapter = PresetCycleAdapter(context!!)
        presetAdapter = PresetAdapter(context!!)

        val btn_allpsi = v.findViewById(R.id.btn_allpsi) as Button
        btn_allpsi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано psi", Toast.LENGTH_LONG).show();

                alldivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (alldivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = alldivider.text.toString().toIntOrNull() ?: -1
                        number = 0
                        type = "psi"
                    }
                    true
                }
                allminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = allminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                allmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = allmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_allproc = v.findViewById(R.id.btn_allproc) as Button
        btn_allproc.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано proc", Toast.LENGTH_LONG).show();

                alldivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (alldivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = alldivider.text.toString().toIntOrNull() ?: -1
                        number = 0
                        type = "proc"
                    }
                    true
                }
                allminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = allminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                allmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = allmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
            }
            false
        })
        val btn_allpop = v.findViewById(R.id.btn_allpop) as Button
        btn_allpop.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано pop", Toast.LENGTH_LONG).show();

                alldivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (alldivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = alldivider.text.toString().toIntOrNull() ?: -1
                        number = 0
                        type = "pop"
                    }
                    true
                }
                allminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = allminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                allmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (allmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = allmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
            }
            false
        })

        val btn_FLpsi = v.findViewById(R.id.btn_FLpsi) as Button
        btn_FLpsi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано psi", Toast.LENGTH_LONG).show();

                FLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FLdivider.text.toString().toIntOrNull() ?: -1
                        number = 1
                        type = "psi"
                    }
                    true
                }
                FLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
            }
            false
        })
        val btn_FLproc = v.findViewById(R.id.btn_FLproc) as Button
        btn_FLproc.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано proc", Toast.LENGTH_LONG).show();

                FLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FLdivider.text.toString().toIntOrNull() ?: -1
                        number = 1
                        type = "proc"
                    }
                    true
                }
                FLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_FLpop = v.findViewById(R.id.btn_FLpop) as Button
        btn_FLpop.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано pop", Toast.LENGTH_LONG).show();

                FLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FLdivider.text.toString().toIntOrNull() ?: -1
                        number = 1
                        type = "pop"
                    }
                    true
                }
                FLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
            }
            false
        })

        val btn_FRpsi = v.findViewById(R.id.btn_FRpsi) as Button
        btn_FRpsi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано psi", Toast.LENGTH_LONG).show();

                FRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FRdivider.text.toString().toIntOrNull() ?: -1
                        number = 2
                        type = "psi"
                    }
                    true
                }
                FRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_FRproc = v.findViewById(R.id.btn_FRproc) as Button
        btn_FRproc.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано proc", Toast.LENGTH_LONG).show();

                FRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FRdivider.text.toString().toIntOrNull() ?: -1
                        number = 2
                        type = "proc"
                    }
                    true
                }
                FRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_FRpop = v.findViewById(R.id.btn_FRpop) as Button
        btn_FRpop.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано pop", Toast.LENGTH_LONG).show();

                FRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = FRdivider.text.toString().toIntOrNull() ?: -1
                        number = 2
                        type = "pop"
                    }
                    true
                }
                FRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = FRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                FRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (FRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = FRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })

        val btn_RLpsi = v.findViewById(R.id.btn_RLpsi) as Button
        btn_RLpsi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано psi", Toast.LENGTH_LONG).show();
                RLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RLdivider.text.toString().toIntOrNull() ?: -1
                        number = 3
                        type = "psi"
                    }
                    true
                }
                RLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_RLproc = v.findViewById(R.id.btn_RLproc) as Button
        btn_RLproc.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано proc", Toast.LENGTH_LONG).show();

                RLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RLdivider.text.toString().toIntOrNull() ?: -1
                        number = 3
                        type = "proc"
                    }
                    true
                }
                RLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_RLpop = v.findViewById(R.id.btn_RLpop) as Button
        btn_RLpop.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано pop", Toast.LENGTH_LONG).show();

                RLdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RLdivider.text.toString().toIntOrNull() ?: -1
                        number = 3
                        type = "pop"
                    }
                    true
                }
                RLminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RLminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RLmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RLmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RLmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })

        val btn_RRpsi = v.findViewById(R.id.btn_RRpsi) as Button
        btn_RRpsi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано psi", Toast.LENGTH_LONG).show();
                RRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RRdivider.text.toString().toIntOrNull() ?: -1
                        number = 4
                        type = "psi"
                    }
                    true
                }
                RRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_RRproc = v.findViewById(R.id.btn_RRproc) as Button
        btn_RRproc.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано proc", Toast.LENGTH_LONG).show();

                RRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RRdivider.text.toString().toIntOrNull() ?: -1
                        number = 4
                        type = "proc"
                    }
                    true
                }
                RRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })
        val btn_RRpop = v.findViewById(R.id.btn_RRpop) as Button
        btn_RRpop.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(context!!, "Выбрано pop", Toast.LENGTH_LONG).show();

                RRdivider.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRdivider.text.toString().toIntOrNull() ?: -1 != divider) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        divider = RRdivider.text.toString().toIntOrNull() ?: -1
                        number = 4
                        type = "pop"
                    }
                    true
                }
                RRminValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRminValue.text.toString().toIntOrNull() ?: -1 != minValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        minValue = RRminValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }
                RRmaxValue.setOnEditorActionListener { textView, i, keyEvent ->
                    if (RRmaxValue.text.toString().toIntOrNull() ?: -1 != maxValue) {
                        Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                        maxValue = RRmaxValue.text.toString().toIntOrNull() ?: -1
                    }
                    true
                }

            }
            false
        })

// Выводит нужное колличество полей для характеристики цыклов
        kolCyc.setOnEditorActionListener { textView, i, keyEvent ->
            if (kolCyc.text.toString().toIntOrNull() ?: -1 != oldkolCyc) {
                Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                oldkolCyc = kolCyc.text.toString().toIntOrNull() ?: -1
                presetCycleAdapter.count = oldkolCyc
                listView.adapter = presetCycleAdapter
            }
            true
        }
// Выводит нужное колличество полей для характеристики цыклов
        kolPre.setOnEditorActionListener { textView, i, keyEvent ->
            if (kolPre.text.toString().toIntOrNull() ?: -1 != oldkolPre) {
                Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                oldkolPre = kolPre.text.toString().toIntOrNull() ?: -1
                presetAdapter.count = oldkolPre
                listPreset.adapter = presetAdapter
            }
            true
        }

        editupThreshold.setOnEditorActionListener { textView, i, keyEvent ->
            if (editupThreshold.text.toString().toIntOrNull() ?: -1 != upThreshold) {
                Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                upThreshold = editupThreshold.text.toString().toIntOrNull() ?: -1
            }
            true
        }
        editdownThreshold.setOnEditorActionListener { textView, i, keyEvent ->
            if (editdownThreshold.text.toString().toIntOrNull() ?: -1 != downThreshold) {
                Toast.makeText(context!!, "перезаписано", Toast.LENGTH_LONG).show();
                downThreshold = editdownThreshold.text.toString().toIntOrNull() ?: -1
            }
            true
        }
        loadSettings("{\"AllCarSettings\":{\"Sensors\":" +
                "{\"CompressorPressure\":{\"number\":0,\"type\":\"psi\",\"divider\":16,\"minValue\":20,\"maxValue\":250}," +
                "\"FrontLeft\":{\"number\":1,\"type\":\"psi\",\"divider\":16,\"minValue\":20,\"maxValue\":250}," +
                "\"FrontRight\":{\"number\":2,\"type\":\"psi\",\"divider\":16,\"minValue\":20,\"maxValue\":250}," +
                "\"RearLeft\":{\"number\":3,\"type\":\"psi\",\"divider\":16,\"minValue\":20,\"maxValue\":250}," +
                "\"RearRight\":{\"number\":4,\"type\":\"psi\",\"divider\":16,\"minValue\":20,\"maxValue\":250}}," +

                "\"PresetsValues\":[" +
                "{\"FrontLeft\":10,\"FrontRight\":10,\"RearLeft\":10,\"RearRight\":10}," +
                "{\"FrontLeft\":100,\"FrontRight\":100,\"RearLeft\":100,\"RearRight\":100}," +
                "{\"FrontLeft\":200,\"FrontRight\":200,\"RearLeft\":200,\"RearRight\":200}," +
                "{\"FrontLeft\":1000,\"FrontRight\":1000,\"RearLeft\":1000,\"RearRight\":1000}]," +

                "\"PresetsCycles\":[" +
                "{\"threshold\":256,\"upDuration\":1000,\"downDuration\":500,\"afterDelay\":200}," +
                "{\"threshold\":76,\"upDuration\":200,\"downDuration\":100,\"afterDelay\":200}," +
                "{\"threshold\":24,\"upDuration\":50,\"downDuration\":30,\"afterDelay\":100}]," +

                "\"CompressorPressure\":" +
                "{\"upThreshold\":3000,\"downThreshold\":2800}}}")
    }
    private fun checkGPSIsOpen(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager ?: return false
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun loadSettings(msg: String): Any {
        val any = try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            var jsonData: Map<String, Any> =
                gson.fromJson(msg, object : TypeToken<Map<String, Any>>() {}.type)

            val AllCarSettings: LinkedTreeMap<String, Any> =
                jsonData["AllCarSettings"] as LinkedTreeMap<String, Any>
            val Sensors: LinkedTreeMap<String, Any> =
                AllCarSettings["Sensors"] as LinkedTreeMap<String, Any>
            val CompressorPressure: LinkedTreeMap<String, Any> =
                Sensors["CompressorPressure"] as LinkedTreeMap<String, Any>
            val alldividerjson: Int =
                (CompressorPressure["divider"] as Double).toInt()
            val allminValuejson: Int =
                (CompressorPressure["minValue"] as Double).toInt()
            val allmaxValuejson: Int =
                (CompressorPressure["maxValue"] as Double).toInt()
            allminValue.text = alldividerjson.toString()
            alldivider.text = allminValuejson.toString()
            allmaxValue.text = allmaxValuejson.toString()

            val FrontLeft: LinkedTreeMap<String, Any> =
                Sensors["FrontLeft"] as LinkedTreeMap<String, Any>
            val FLdividerjson: Int =
                (FrontLeft["divider"] as Double).toInt()
            val FLminValuejson: Int =
                (FrontLeft["minValue"] as Double).toInt()
            val FLmaxValuejson: Int =
                (FrontLeft["maxValue"] as Double).toInt()
            FLminValue.text = FLdividerjson.toString()
            FLdivider.text = FLminValuejson.toString()
            FLmaxValue.text = FLmaxValuejson.toString()

            val FrontRight: LinkedTreeMap<String, Any> =
                Sensors["FrontRight"] as LinkedTreeMap<String, Any>
            val FRdividerjson: Int =
                (FrontRight["divider"] as Double).toInt()
            val FRminValuejson: Int =
                (FrontRight["minValue"] as Double).toInt()
            val FRmaxValuejson: Int =
                (FrontRight["maxValue"] as Double).toInt()
            FRminValue.text = FRdividerjson.toString()
            FRdivider.text = FRminValuejson.toString()
            FRmaxValue.text = FRmaxValuejson.toString()

            val RearLeft: LinkedTreeMap<String, Any> =
                Sensors["RearLeft"] as LinkedTreeMap<String, Any>
            val RLdividerjson: Int =
                (RearLeft["divider"] as Double).toInt()
            val RLminValuejson: Int =
                (RearLeft["minValue"] as Double).toInt()
            val RLmaxValuejson: Int =
                (RearLeft["maxValue"] as Double).toInt()
            RLminValue.text = RLdividerjson.toString()
            RLdivider.text = RLminValuejson.toString()
            RLmaxValue.text = RLmaxValuejson.toString()

            val RearRight: LinkedTreeMap<String, Any> =
                Sensors["RearRight"] as LinkedTreeMap<String, Any>
            val RRdividerjson: Int =
                (RearRight["divider"] as Double).toInt()
            val RRminValuejson: Int =
                (RearRight["minValue"] as Double).toInt()
            val RRmaxValuejson: Int =
                (RearRight["maxValue"] as Double).toInt()
            RRminValue.text = RRdividerjson.toString()
            RRdivider.text = RRminValuejson.toString()
            RRmaxValue.text = RRmaxValuejson.toString()

            val CompressorPressure1: LinkedTreeMap<String, Any> =
                Sensors["CompressorPressure"] as LinkedTreeMap<String, Any>
            val upThresholdjson: Int =
                (CompressorPressure1["upThreshold"] as Double).toInt()
            val downThresholdjson: Int =
                (CompressorPressure1["downThreshold"] as Double).toInt()
            editupThreshold.text = Editable.Factory.getInstance().newEditable(upThresholdjson.toString())
            editdownThreshold.text = Editable.Factory.getInstance().newEditable(downThresholdjson.toString())

            val PresetsValues: ArrayList<LinkedTreeMap<String, Any>> =
                AllCarSettings["PresetsValues"] as ArrayList<LinkedTreeMap<String, Any>>
            presetAdapter.count=PresetsValues.count()
            for(i in 0 until PresetsValues.count()){
                val FrontLeftjson: Int =
                    (PresetsValues[i]["FrontLeft"] as Double).toInt()
                val FrontRightjson: Int =
                    (PresetsValues[i]["FrontRight"] as Double).toInt()
                val RearLeftjson: Int =
                    (PresetsValues[i]["RearLeft"] as Double).toInt()
                val RearRightjson: Int =
                    (PresetsValues[i]["RearRight"] as Double).toInt()

                (presetAdapter.getItem(i) as Preset).FL = FrontLeftjson;
                (presetAdapter.getItem(i) as Preset).FR = FrontLeftjson;
                (presetAdapter.getItem(i) as Preset).RL = FrontLeftjson;
                (presetAdapter.getItem(i) as Preset).RR = FrontLeftjson;

            }
//            presetAdapter.FL.text =  Editable.Factory.getInstance().newEditable(FrontLeftjson.toString())
//            presetAdapter.FR.text = Editable.Factory.getInstance().newEditable(FrontRightjson.toString())
//            presetAdapter.RL.text =  Editable.Factory.getInstance().newEditable(RearLeftjson.toString())
//            presetAdapter.RR.text = Editable.Factory.getInstance().newEditable(RearRightjson.toString())

            val PresetsCycles: LinkedTreeMap<String, Any> =
                AllCarSettings["PresetsCycles"] as LinkedTreeMap<String, Any>
            val thresholdjson: Int =
                (PresetsCycles["threshold"] as Double).toInt()
            val upDurationjson: Int =
                (PresetsCycles["upDuration"] as Double).toInt()
            val downDurationjson: Int =
                (PresetsCycles["downDuration"] as Double).toInt()
            val afterDelayjson: Int =
                (PresetsCycles["afterDelay"] as Double).toInt()

//            presetCycleAdapter.text =  Editable.Factory.getInstance().newEditable(thresholdjson.toString())
//            presetCycleAdapter.text = Editable.Factory.getInstance().newEditable(upDurationjson.toString())
//            presetCycleAdapter.text = Editable.Factory.getInstance().newEditable(downDurationjson.toString())
//            presetCycleAdapter.text = Editable.Factory.getInstance().newEditable(afterDelayjson.toString())

        } catch (e: Exception) {
            var a = 0;
            a++;
        }
        return any
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, prefs: SharedPreferences) =
            CustomizationFragment(prefs).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


//{
//    "AllCarSettings": {
//    "Sensors": {
//    "CompressorPressure": {
//    "number": 0,
//    "type": "psi",
//    "divider": 16,
//    "minValue": 16,
//    "maxValue": 250
//},
//    "FrontLeft": {
//    "number": 1,
//    "type": "psi",
//    "divider": 16,
//    "minValue": 16,
//    "maxValue": 250
//},
//    "FrontRight": {
//    "number": 2,
//    "type": "psi",
//    "divider": 16,
//    "minValue": 16,
//    "maxValue": 250
//},
//    "RearLeft": {
//    "number": 3,
//    "type": "psi",
//    "divider": 16,
//    "minValue": 16,
//    "maxValue": 250
//},
//    "RearRight": {
//    "number": 4,
//    "type": "psi",
//    "divider": 16,
//    "minValue": 16,
//    "maxValue": 250
//}
//},
//    "PresetsValues": [
//    {
//        "FrontLeft": 10,
//        "FrontRight": 10,
//        "RearLeft": 10,
//        "RearRight": 10
//    },
//    {
//        "FrontLeft": 100,
//        "FrontRight": 100,
//        "RearLeft": 100,
//        "RearRight": 100
//    },
//    {
//        "FrontLeft": 200,
//        "FrontRight": 200,
//        "RearLeft": 200,
//        "RearRight": 200
//    },
//    {
//        "FrontLeft": 1000,
//        "FrontRight": 1000,
//        "RearLeft": 1000,
//        "RearRight": 1000
//    }
//    ],
//    "PresetsCycles": [
//    {
//        "threshold": 256,
//        "upDuration": 1000,
//        "downDuration": 500,
//        "afterDelay": 200
//    },
//    {
//        "threshold": 76,
//        "upDuration": 200,
//        "downDuration": 100,
//        "afterDelay": 200
//    },
//    {
//        "threshold": 24,
//        "upDuration": 50,
//        "downDuration": 30,
//        "afterDelay": 100
//    }
//    ],
//    "CompressorPressure": {
//    "upThreshold": 3000,
//    "downThreshold": 2800
//}
//}
//}