package fr.isen.langeron.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

class HomeActivity : AppCompatActivity() {

    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        cycleButton.setOnClickListener {
            val intent = Intent(this, cycle_de_vie::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext,"Identification réussi !",Toast.LENGTH_SHORT).show()
        }


        webButton.setOnClickListener {
            val intent = Intent(this, WebServiceActivity::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext,"Identification réussi !",Toast.LENGTH_SHORT).show()
        }


        permissionButton.setOnClickListener {
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext,"Identification réussi !",Toast.LENGTH_SHORT).show()
        }


        sauvegardeButton.setOnClickListener {
            val intent = Intent(this, FormulaireActivity::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext,"Identification réussi !",Toast.LENGTH_SHORT).show()
        }


        decoButton.setOnClickListener {
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}
