package fr.isen.langeron.androidtoolbox

import android.bluetooth.BluetoothGatt
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
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
import kotlinx.android.synthetic.main.activity_web_service_cell.view.*


class BLEServiceAdapter(serviceList: MutableList<BLEService>,
                        var context: Context,
                        gatt: BluetoothGatt?) :
    ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharacteristicViewHolder>(serviceList) {

    val ble: BluetoothGatt? = gatt


    class ServiceViewHolder(itemView : View): GroupViewHolder(itemView){
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




    class CharacteristicViewHolder(itemView : View): ChildViewHolder(itemView){
        val charactUUID: TextView = itemView.charactUUID
        val charactNom: TextView = itemView.charactNom}

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder (

            LayoutInflater.from(parent.context).inflate(R.layout.activity_bledevice_details_cell, parent, false)
            )


    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder= CharacteristicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_bledevice_details_cell,parent, false))



    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val charact: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]
        val uuid = charact.uuid

        holder.charactUUID.text = uuid.toString()
     //  holder.charactNom.text =  name ??

      //  ??????
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = group.title
    }
}

