package fr.isen.langeron.androidtoolbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_permission_cell.view.*


class ContactAdapter(val contacts : List<String>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_permission_cell, parent, false))


    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contactName.text=contacts[position]
    }

    class ContactViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val contactName: TextView = contactView.contactName
    }



}
