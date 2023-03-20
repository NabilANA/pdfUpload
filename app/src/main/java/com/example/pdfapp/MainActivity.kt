package com.example.pdfapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class MainActivity : AppCompatActivity() {


    lateinit var uri: Uri
    lateinit var mStorage : StorageReference
    val PDF : Int = 0
    private var currentFile: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uploadBtn = findViewById<View>(R.id.uploadBtn) as Button
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

        uploadBtn.setOnClickListener {

                view: View? ->
            val intent = Intent()
            intent.setType("pdf/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
        }

        override fun onActivityReenter(resultCode: Int, data: Intent?) {
            val  uriText = findViewById<View>(R.id.uriText)as TextView
            if (resultCode == RESULT_OK){
                if (resultCode == PDF){
                    uri = data!!.data
                    uriText.text = uri.toString()
                    upload ()
                }
            }
            super.onActivityReenter(resultCode, data)
        }
        private fun  upload() {
            var mReference = uri.lastPathSegment?.let { mStorage.child(it) }
            if (mReference != null) {
                mReference.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? -> var url = taskSnapshot!!.uploadSessionUri
                    val dwnText = findViewById<View>(R.id.dwnText)as TextView
                    dwnText.text = url.toString()
                    Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}