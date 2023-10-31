package com.cube.pruebatecnicagrupopromass

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cube.Entry
import com.cube.pruebatecnicagrupopromass.adapters.ContentAdapter
import com.cube.pruebatecnicagrupopromass.adapters.MyAdapter
import com.cube.pruebatecnicagrupopromass.dao.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    private val TAG = AppDatabase::class.simpleName
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.list)
        floatingActionButton = findViewById(R.id.button_add)
        toolbar = findViewById(R.id.toolbar)

        if (internetCheck(this)) {
            getPosts()
            floatingActionButton.visibility = View.VISIBLE
        } else {
            getPostsOffline()
            floatingActionButton.visibility = View.GONE
        }


        Log.d(TAG, recyclerView.size.toString())

        floatingActionButton.setOnClickListener {
            goToCreate()
        }

        setSupportActionBar(toolbar)
    }

    private fun goToCreate() {
        val intent = Intent(this, CreateContentActivity::class.java)
        startActivity(intent)
    }

    private fun getPosts() {
        val db = Firebase.firestore
        val entries: ArrayList<Entry> = ArrayList()
        val dt = SimpleDateFormat("dd-MM-yyyy")
        val today = dt.format(Date()).toString()
        db.collection("entries")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val entry = Entry()
                    entry.id = document.id
                    entry.author = document.get("author").toString()
                    entry.content = document.get("content").toString()
                    entry.postedDate = today
                    entry.title = document.get("title").toString()

                    entries.add(entry)

                    val author: String = document.get("author").toString()
                    Log.d(TAG, author + "")
                    Log.d(TAG, entry.content)
                }
                val adapter =  MyAdapter(entries, this);
                val llm = LinearLayoutManager(this)
                llm.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = llm
                recyclerView.adapter = adapter
            }
            .addOnFailureListener{ exception ->
                exception.printStackTrace()
                Log.e(TAG, "Error getting docs", exception)
            }
            .addOnCanceledListener {
                Log.e(TAG, "Canceled")
            }

        val size = entries.size
        Log.d(TAG, size.toString())

    }

    private fun getPostsOffline() {
        var db = AppDatabase.getInstance(this)
        var entries = db.entryDao().getAll()

        val adapter =  MyAdapter(entries, this);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        getPosts()
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

