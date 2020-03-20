package fr.isen.langeron.androidtoolbox

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_web_service.*


class WebServiceActivity : AppCompatActivity() {

    private val url = "https://randomuser.me/api/?results=100&nat=fr"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service)
        Api()
    }
        private fun Api(): RandomUser {
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            var Randomuser = RandomUser()
            val stringRequest = JsonObjectRequest(
                Request.Method.GET,
                "https://randomuser.me/api/?results=100&nat=fr",
                null,
                Response.Listener { response ->
                    val gson = Gson()
                    Randomuser = gson.fromJson(response.toString(), RandomUser::class.java)

                    Log.d("USER", Randomuser.results.toString())

                    //progressBar2.visibility = View.INVISIBLE
                    contactService.layoutManager = LinearLayoutManager(this)
                    contactService.adapter = WebServiceAdapter(Randomuser,this)
                    contactService.visibility = View.VISIBLE
                },
                Response.ErrorListener {
                    Log.d("TAG", "Error")
                }
            )
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
            return Randomuser
        }

    }



class RandomUser {
    val results: ArrayList<Result> = ArrayList()
}