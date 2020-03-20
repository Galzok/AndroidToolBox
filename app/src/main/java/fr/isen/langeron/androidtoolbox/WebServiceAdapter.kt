package fr.isen.langeron.androidtoolbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_permission_cell.view.*
import kotlinx.android.synthetic.main.activity_web_service_cell.view.*

class WebServiceAdapter (val userList : RandomUser, val context : Context) : RecyclerView.Adapter<WebServiceAdapter.WebServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebServiceViewHolder =
        WebServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_web_service_cell, parent, false)
                ,userList
        )


    override fun getItemCount(): Int = userList.results.size

    override fun onBindViewHolder(holder: WebServiceViewHolder, position: Int) {
        holder.loadInfo(position)


    }

        class WebServiceViewHolder(userView: View, val userList: RandomUser) : RecyclerView.ViewHolder(userView) {
            val textName: TextView = userView.textName
            val textAdresse: TextView = userView.textAdresse
            val textMail: TextView = userView.textMail
            val image: ImageView = userView.imageWebService



            fun loadInfo(position: Int) {
                val nameUser =
                    userList.results[position].name.first + " " + userList.results[position].name.last
                val addressUser =
                    userList.results[position].location.city

                Picasso.get()
                    .load(userList.results[position].picture.large)
                    .fit().centerInside()
                    .into(image)

                textName.text = nameUser
                textMail.text = userList.results[position].email
                textAdresse.text = addressUser

            }
        }

    }




