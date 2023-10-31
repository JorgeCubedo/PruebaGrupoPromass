package com.cube.pruebatecnicagrupopromass

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.cube.Entry
import com.cube.pruebatecnicagrupopromass.dao.AppDatabase

class EntryActivity : AppCompatActivity() {
    private val TAG = EntryActivity::class.simpleName
    private lateinit var txtContent: TextView
    private lateinit var txtAuthor: TextView
    private lateinit var txtDate: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var btnSaveEntry: Button
    private lateinit var entry: Entry

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        initUI()

        toolbar.setNavigationOnClickListener{
            finish()
        }
        if (!internetCheck(this)) {
            Toast.makeText(this, "No hay conexion a internet.", Toast.LENGTH_LONG).show()
            btnSaveEntry.visibility = View.GONE
        }
        var bundle: Bundle? = intent.extras

        var id = bundle?.getString("EntryId", null)
        var title = bundle?.getString("EntryTitle", null)
        var content = bundle?.getString("EntryContent", null)
        var author = bundle?.getString("EntryAuthor", null)
        var date = bundle?.getString("EntryDate", null)

        if (!id.isNullOrEmpty()
            || !title.isNullOrEmpty()
            || !content.isNullOrEmpty()
            || !author.isNullOrEmpty()
            || !date.isNullOrEmpty()) {
            //No se pq me tira error si quito ese if
            if (id != null) {
                entryExists(id)
            }

            toolbar.title = title
            txtContent.text = content
            txtDate.text = "Fecha de publicacion: $date"
            txtAuthor.text = "Autor: $author"

            entry = Entry()
            entry.id = id.toString()
            entry.author = author.toString()
            entry.title = title.toString()
            entry.content = content.toString()
            entry.postedDate = date.toString()
        } else {
            Log.d(TAG, "ID is null")
            Toast.makeText(this, "Error obteniendo la informacion.", Toast.LENGTH_LONG).show()
            finish()
        }

        btnSaveEntry.setOnClickListener {
            saveEntry(entry)
        }
    }

    private fun initUI() {
        toolbar = findViewById(R.id.toolbar)
        txtContent = findViewById(R.id.text_content)
        txtAuthor = findViewById(R.id.text_author)
        txtDate = findViewById(R.id.text_date)
        btnSaveEntry = findViewById(R.id.button_save_entry)
    }

    private fun saveEntry(entry: Entry) {
        try {
            var db = AppDatabase.getInstance(this)
            db.entryDao().insertEntry(entry)

            Toast.makeText(this, "Entrada guardada correctamente", Toast.LENGTH_LONG).show()

            btnSaveEntry.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Error al guardar entrada, intente mas tarde", Toast.LENGTH_LONG).show()
        }

    }

     private fun entryExists(id: String) {
        var db = AppDatabase.getInstance(this)
        var e = db.entryDao().getEntry(id)

        if (e != null)
            btnSaveEntry.visibility = View.GONE
        else
            btnSaveEntry.visibility = View.VISIBLE
    }

    fun internetCheck(c: Context): Boolean {
        val cmg = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            cmg.getNetworkCapabilities(cmg.activeNetwork)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            return cmg.activeNetworkInfo?.isConnectedOrConnecting == true
        }

        return false
    }
}