package com.cube.pruebatecnicagrupopromass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.cube.Entry
import com.cube.pruebatecnicagrupopromass.dao.AppDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateContentActivity : AppCompatActivity() {
    private val TAG = CreateContentActivity::class.simpleName
    private lateinit var edtTitle: EditText
    private lateinit var edtAuthor: EditText
    private lateinit var edtContent: EditText
    private lateinit var btnFinalize: Button
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_content)

        initUI()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnFinalize.setOnClickListener{
            saveEntry()
        }
    }

    private fun initUI() {
        edtTitle = findViewById(R.id.edit_title)
        edtAuthor = findViewById(R.id.edit_author)
        edtContent = findViewById(R.id.edit_content)
        btnFinalize = findViewById(R.id.button_finalize)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun saveEntry() {
        if (!edtTitle.text.isEmpty() && !edtAuthor.text.isEmpty() && !edtContent.text.isEmpty()) {
            val db = Firebase.firestore

            val entry = Entry()
            entry.title = edtTitle.text.toString()
            entry.author = edtAuthor.text.toString()
            entry.content = edtContent.text.toString()
            entry.postedDate = Date().toString()
            try {
                val ent = hashMapOf(
                    "title" to entry.title,
                    "author" to entry.author,
                    "content" to entry.content,
                    "posted_date" to Date().toString()
                )

                db.collection("entries")
                    .add(ent)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Success")
                        finish()
                    }
                    .addOnFailureListener { e->
                        Log.e(TAG, "Error adding entry", e)
                        Toast.makeText(this, "Error adding entry", Toast.LENGTH_LONG)
                    }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }


        } else {
            Toast.makeText(this, "Please fill all the Text boxes", Toast.LENGTH_LONG).show()
        }

    }
}