package fr.isen.langeron.androidtoolbox

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    val contacts = mutableListOf<String>()

    private var ImageButton: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)


        contactRecycler.adapter = ContactAdapter(listOf("Tim","Armand"))
        contactRecycler.layoutManager = LinearLayoutManager(this)

        getContacts()

        listContact.adapter = ContactAdapter(contacts.sorted())
        listContact.layoutManager = LinearLayoutManager(this)


        imageButton.setOnClickListener { showPictureDialog() }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            imageButton.setImageURI(data?.data)
        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA) {
            var bmp = data?.extras?.get("data") as Bitmap
            imageButton.setImageBitmap(bmp)
        }
    }

    private fun getContacts() {
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contacts.add("  Nom : $name")
            }
        } else {
            Toast.makeText(applicationContext, "No contacts available!", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

}

