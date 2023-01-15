package ru.airmasters.airstd_base

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.widget.Toast
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import java.util.*

class BleDeviceAdapter private constructor(
    private val bleDevice: BleDevice,
    private val ErrorHandler: (String) -> Unit,
    private val ControlService: BluetoothGattService,
    private val ControlCharacteristic: BluetoothGattCharacteristic
) {



    private var WriteEnded: Boolean = true;
    private var WriteBuff: ByteArray = byteArrayOf();

    fun write(cmd: ByteArray){
        try {
            if (WriteEnded) {
                WriteEnded = false;
                BleManager.getInstance().write(
                    bleDevice,
                    ControlCharacteristic!!.getService().uuid.toString(),
                    ControlCharacteristic!!.getUuid().toString(),
                    cmd,
                    object : BleWriteCallback() {
                        override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray?) {
                            WriteEnded = true;
                            if (WriteBuff.isNotEmpty()) {
                                write(WriteBuff);
                            }
                            WriteBuff = byteArrayOf();
                        }

                        override fun onWriteFailure(exception: BleException) {
//                            Toast.makeText(context!!, "Отправка комманды не удалась", Toast.LENGTH_SHORT).show()
                            ErrorHandler("Отправка комманды не удалась");
                        }
                    })
            } else {
                WriteBuff += cmd;
            }
        }catch (e: Exception){
//            Toast.makeText(context!!, e.toString(), Toast.LENGTH_LONG).show()
            ErrorHandler(e.toString());
            e.printStackTrace()
        }
    }

    var notifyMessage: String = ""
    fun openNotify(MessageHandler: (String) -> Unit){
        BleManager.getInstance().notify(
            bleDevice,
            ControlServiceUUID.toString(),
            ControlCharacteristicUUID.toString(),
            object : BleNotifyCallback() {

                override fun onNotifySuccess() {

                }

                override fun onNotifyFailure(exception: BleException) {
                    ErrorHandler("Возникла ошибка при подключении к оповещениям. Возможно, вы подключились не к машине, или к машине ранней версии.")
                }

                override fun onCharacteristicChanged(data: ByteArray) {
                    val text: String = String(data)
                    notifyMessage += text
                    if (text[text.length-1] == '\n') {
                        MessageHandler(notifyMessage)
                        notifyMessage = ""
                    }
                }
            })

    }


    companion object{
        private val ControlServiceUUID: List<UUID> = arrayListOf(UUID.fromString("0000a910-0000-1000-8000-00805f9b34fb"), UUID.fromString("c755b47b-d436-4c25-a7cf-828680863dd3")); //OLD:"c755b47b-d436-4c25-a7cf-828680863dd3"
        private val ControlCharacteristicUUID: List<UUID> = arrayListOf(UUID.fromString("0000a911-0000-1000-8000-00805f9b34fb"), UUID.fromString("ad9ee860-7105-42e0-8e8c-afb358f74112")); // OLD:"ad9ee860-7105-42e0-8e8c-afb358f74112"

        fun construct(bleDevice: BleDevice, ErrorHandler: (String) -> Unit): BleDeviceAdapter?{
            val gatt = BleManager.getInstance().getBluetoothGatt(bleDevice) ?: return null

            var ControlService: BluetoothGattService? = null
            var ControlCharacteristic: BluetoothGattCharacteristic? = null
            for (service in gatt.services) {

                if(ControlServiceUUID.contains(service.uuid)){
                    ControlService = service;
                    for (characteristic in service.characteristics) {
                        if (ControlCharacteristicUUID.contains(characteristic.uuid)) {
                            ControlCharacteristic = characteristic
                            break
                        }
                    }
                }
            }
            if(ControlService == null || ControlCharacteristic == null){
                ErrorHandler("Возникла ошибка. Возможно, вы подключились не к машине.")
                return null
            }
            return BleDeviceAdapter(bleDevice, ErrorHandler, ControlService, ControlCharacteristic)
        }
    }

}