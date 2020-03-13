package fr.isen.langeron.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cycle_de_vie.*
import kotlinx.android.synthetic.main.activity_home.*

class cycle_de_vie : AppCompatActivity() {

    private var texte :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle_de_vie)
        texte += "onCreate()\n"
        cycle_de_vieTextView.text = texte
    }

    override fun onPause() {
        super.onPause()
        texte += "onPause()\n"
        cycle_de_vieTextView.text = texte
    }

    override fun onResume() {
        super.onResume()
        texte += "onResume()\n"
        cycle_de_vieTextView.text = texte
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"onDestroy()", Toast.LENGTH_SHORT).show()
    }
}