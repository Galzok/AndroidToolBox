package fr.isen.langeron.androidtoolbox

import android.app.AlertDialog
import android.bluetooth.BluetoothGatt
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.activity_bledevice_details_cell.view.*
import kotlinx.android.synthetic.main.activity_bledevice_characteristic_cell.view.*
import kotlinx.android.synthetic.main.activity_bledevice_dialog.view.*
import java.util.*


class BLEServiceAdapter(
    serviceList: MutableList<BLEService>,
    var context: Context,
    gatt: BluetoothGatt?
) :
    ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharacteristicViewHolder>(
        serviceList
    ) {

    val ble: BluetoothGatt? = gatt
    var notifier = false

    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val fleche: ImageView = itemView.imageFleche

        val serviceUuid: TextView = itemView.textUUID
        val nomService: TextView = itemView.textNom

        override fun expand() {
            animateExpand()
        }

        override fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate = RotateAnimation(
                360F,
                180F,
                RELATIVE_TO_SELF,
                0.5f,
                RELATIVE_TO_SELF,
                0.5f
            )
            rotate.duration = 300
            rotate.fillAfter = true
            fleche.animation = rotate
        }

        private fun animateCollapse() {
            val rotate = RotateAnimation(
                180F,
                360F,
                RELATIVE_TO_SELF,
                0.5f,
                RELATIVE_TO_SELF,
                0.5f
            )
            rotate.duration = 300
            rotate.fillAfter = true
            fleche.animation = rotate
        }
    }


    class CharacteristicViewHolder(itemView: View) : ChildViewHolder(itemView) {
        val charactUUID: TextView = itemView.charactUUID
        val charactNom: TextView = itemView.charactNom
        val properties: TextView = itemView.properties
        val valueBle: TextView = itemView.charactValue
        val buttonRead: TextView = itemView.readButtonON
        val buttonWrite: TextView = itemView.writeButtonON
        val buttonNotify: TextView = itemView.notifyButtonON
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_bledevice_details_cell, parent, false)
        )


    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder = CharacteristicViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.activity_bledevice_details_cell,
            parent,
            false
        )
    )


    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]
        //Log.d("carac", characteristic.getStringValue(0))

        holder.buttonRead.visibility = View.GONE
        holder.buttonWrite.visibility = View.GONE
        holder.buttonNotify.visibility = View.GONE

        if (proprietie(characteristic.properties).contains("Lire")) {
            holder.buttonRead.visibility = View.VISIBLE
        }
        if (proprietie(characteristic.properties).contains("Ecrire")) {
            holder.buttonWrite.visibility = View.VISIBLE
        }
        if (proprietie(characteristic.properties).contains("Notifier")) {
            holder.buttonNotify.visibility = View.VISIBLE
        }


        val uuid = characteristic.uuid
        val nom = propertyNom(uuid)

        holder.charactUUID.text = uuid.toString()
        holder.charactNom.text = nom
        holder.properties.text = "Proprietés : ${proprietie(characteristic.properties)}"
        ble?.readCharacteristic(characteristic)
        holder.valueBle.text = "Valeur : "

        holder.buttonRead.setOnClickListener {
            ble?.readCharacteristic(characteristic)
            if (characteristic.value != null) {
                holder.valueBle.text = "Valeur : ${String(characteristic.value)}"
            } else {
                holder.valueBle.text = "Valeur : null"
            }
        }


        holder.buttonWrite.setOnClickListener {
            val dialog = AlertDialog.Builder(context)

            val editView = View.inflate(context, R.layout.activity_bledevice_dialog, null)

            dialog.setView(editView)
            dialog.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which -> })
            dialog.setPositiveButton("Valider", DialogInterface.OnClickListener { _, _ ->
                val text = editView.textDialog.text.toString()
                characteristic.setValue(text)
                ble?.writeCharacteristic(characteristic)
            })
            dialog.show()
        }


        holder.buttonNotify.setOnClickListener {
            if (!notifier){
                notifier = true
                holder.buttonNotify.setBackgroundColor(0x40FF0000)
                if (ble != null) {
                    setCharacteristicNotificationInternal(ble, characteristic, true)
                    if(characteristic.value != null){
                        holder.valueBle.text =  "Valeur : ${byteArrayToHexString(characteristic.value)}"
                    } else {
                        holder.valueBle.text =  "Valeur : null"
                    }
                }
            } else {
                notifier = false
                holder.buttonNotify.setBackgroundColor(0x00FFFFFF)
                if (ble != null) {
                    setCharacteristicNotificationInternal(ble, characteristic, false)
                }
            }
        }
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = group.title
        var uuidNom: String = when (group.title) {
            "00001800-0000-1000-8000-00805f9b34fb" -> "Accès générique"
            "00001801-0000-1000-8000-00805f9b34fb" -> "Attirbut générique"
            else -> "Service spécifique"
        }

        holder.serviceUuid.text = title
        holder.nomService.text = uuidNom
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
    }


    private fun setCharacteristicNotificationInternal(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, enabled: Boolean){
        gatt.setCharacteristicNotification(characteristic, enabled)

        if (characteristic.descriptors.size > 0) {

            val descriptors = characteristic.descriptors
            for (descriptor in descriptors) {

                if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                    descriptor.value = if (enabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                    descriptor.value = if (enabled) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                }
                gatt.writeDescriptor(descriptor)
            }
        }
    }


    private fun proprietie(property: Int): StringBuilder {

        val sb = StringBuilder()

        if (property and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            sb.append("Ecrire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            sb.append(" Lire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            sb.append(" Notifier")
        }
        if (sb.isEmpty()) sb.append("Aucune")

        return sb
    }

    fun propertyNom(uuid: UUID): String {

        var nom: String

        when (uuid.toString().substring(4, 8)) {
            "2a79" -> nom = "Refroidissement éolien"
            else -> nom = "Caractéristique spécifique"
        }
        return nom
    }

}

