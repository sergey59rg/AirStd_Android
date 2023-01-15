package ru.airmasters.airstd_wear

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.clj.fastble.BleManager
import com.clj.fastble.data.BleDevice
import ru.airmasters.airstd_wear.fragment.ConnectionSettingsFragment
import ru.airmasters.airstd_wear.fragment.ServiceListFragment
import ru.airmasters.airstd_wear.observers.Observer
import ru.airmasters.airstd_wear.observers.ObserverManager

class MainActivity : AppCompatActivity(), ConnectionSettingsFragment.OnFragmentInteractionListener,
    Observer {

    private var bleDevice: BleDevice? = null
    private var currentPage = "ServiceListFragment"
    private val fragments = HashMap<String, Fragment>()

    override fun disConnected(device: BleDevice?) {
        if (device != null && bleDevice != null && device.key == bleDevice!!.key) {
            //finish()
            Toast.makeText(
                this,
                "Disconnected",
                Toast.LENGTH_LONG
            ).show()
            (fragments["ServiceListFragment"] as ServiceListFragment).onDisconnected()
        }
    }

    fun changePage(page: String) {
        currentPage = page
        updateFragment(page)
    }

    fun initPage() {
        updateFragment("ConnectionSettingsFragment");
        updateFragment("ServiceListFragment");
    }

    private fun updateFragment(currentFragment: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragments[currentFragment]!! )
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
    }

    override fun onConnected(bledevice: BleDevice?) {
        this.bleDevice = bledevice
        bleDevice?.let { (fragments["ServiceListFragment"] as ServiceListFragment).onConnected(it) }
    }

    override fun onDisconnected(bledevice: BleDevice?) {
        disConnected(bleDevice)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BleManager.getInstance().init(application)

        val sharedPreference = getSharedPreferences("AIRSTD_PREFERENCES", Context.MODE_PRIVATE)
        fragments["ServiceListFragment"] = ServiceListFragment()
        fragments["ConnectionSettingsFragment"] = ConnectionSettingsFragment.newInstance("","", sharedPreference)

        setContentView(R.layout.activity_main)

        initPage()
        ObserverManager.getInstance().addObserver(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentPage != "ServiceListFragment") {
                changePage("ServiceListFragment")
                true
            } else {
                true
            }
        } else super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        super.onDestroy()
        BleManager.getInstance().clearCharacterCallback(bleDevice)
        ObserverManager.getInstance().deleteObserver(this)
    }

    override fun getBleDevice(): BleDevice? {
        return this.bleDevice
    }


}