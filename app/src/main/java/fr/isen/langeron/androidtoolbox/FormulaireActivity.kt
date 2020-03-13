package fr.isen.langeron.androidtoolbox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_formulaire.*
import org.json.JSONObject
import java.io.File
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*


class FormulaireActivity : AppCompatActivity() {

    var age = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)


        save_button.setOnClickListener {

            saveDataToFile(name.text.toString(),
                first_name.text.toString(),
                dateTitle.text.toString())
        }

        read_button.setOnClickListener {
            readDataFromFile()
        }

        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                dateTitle.text = sdf.format(cal.time)

                val today = Calendar.getInstance()
                age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR))
                    age--
            }
        dateTitle.setOnClickListener {
            showDatePicker(dateSetListener)
        }


        dateTitle.setOnClickListener {
            showDatePicker(dateSetListener)
        }


    }

    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this@FormulaireActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun saveDataToFile(lastName: String, firstName: String, date: String) {
        val jsonObject = JSONObject()
        jsonObject.put(KEY_LAST_NAME, lastName)
        jsonObject.put(KEY_FIRST_NAME, firstName)
        jsonObject.put(KEY_BIRTH_DATE, date)
        AlertDialog.Builder(this)
            .setTitle("sauvegarder")

        val data: String = jsonObject.toString()
        File(cacheDir.absolutePath, "user_data.json").writeText(data)
    }

    private fun readDataFromFile() {
        val data = File(cacheDir.absolutePath, "user_data.json").readText()
        val jsonObject = JSONObject(data)

        val lastName = jsonObject.optString(KEY_LAST_NAME)
        val firstName = jsonObject.optString(KEY_FIRST_NAME)
        val dateTitle = jsonObject.optString(KEY_BIRTH_DATE)

        AlertDialog.Builder(this)
            .setTitle("Lecture du fichier")
            .setMessage("Nom : $lastName \n Prenom : $firstName \nDate : $dateTitle \nAge : $age")
            .create()
            .show()
    }


    companion object {
        private const val KEY_LAST_NAME = "lastname"
        private const val KEY_FIRST_NAME = "firstname"
        private const val KEY_BIRTH_DATE = "date"

    }
}





