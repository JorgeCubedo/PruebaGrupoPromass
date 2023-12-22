package com.cube.pruebatecnicajorgecubedo

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cube.Entry
import com.cube.pruebatecnicajorgecubedo.adapters.MyAdapter
import com.cube.pruebatecnicajorgecubedo.dao.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    private val TAG = AppDatabase::class.simpleName
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var searchView: SearchView
    private lateinit var imgFilter: ImageView
    private var filterType: String = "author"
    private lateinit var txtNoEntries: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Se inicializan las variables UI
        recyclerView = findViewById(R.id.list)
        floatingActionButton = findViewById(R.id.button_add)
        searchView = findViewById(R.id.search_view)
        imgFilter = findViewById(R.id.image_filter)
        txtNoEntries = findViewById(R.id.text_no_entries)

        registerForContextMenu(imgFilter) //Para la seleccion de filtro de las entradas
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

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                if (query != null) {
                    Log.d(TAG, "Query: $query")
                    getPostByQuery(query)
                } else {
                    Log.d(TAG, "No Queries")
                    getPosts()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Text Changed: $newText")
                if (newText == "")
                    getPosts()
                return true
            }

        })

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_filter, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_author -> selectAuthorFilter()
            R.id.item_content -> selectContentFilter()
            R.id.item_title -> selectTitleFilter()
        }
        return super.onContextItemSelected(item)
    }

    private fun selectAuthorFilter() {
        imgFilter.setImageResource(R.drawable.baseline_person)
        filterType = "author"
    }

    private fun selectContentFilter() {
        imgFilter.setImageResource(R.drawable.baseline_import_contacts)
        filterType = "content"
    }

    private fun selectTitleFilter() {
        imgFilter.setImageResource(R.drawable.baseline_title)
        filterType = "title"
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
        if (size == 0) {
            recyclerView.visibility = View.GONE
            txtNoEntries.visibility = View.VISIBLE
        }
        Log.d(TAG, size.toString())

    }

    private fun getPostByQuery(query: String) {
        Log.d(TAG, "Filter: $filterType - Query: $query")
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

                    when (filterType) {
                        "author" -> {
                            if (entry.author.lowercase().contains(query.lowercase())) {
                                entries.add(entry)
                            }
                        }
                        "content" -> {
                            if (entry.content.lowercase().contains(query.lowercase()))
                                entries.add(entry)
                        }
                        "title" -> {
                            if (entry.title.lowercase().contains(query.lowercase()))
                                entries.add(entry)
                        }
                    }


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

        if (size == 0) {
            recyclerView.visibility = View.GONE
            txtNoEntries.visibility = View.VISIBLE
        }

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
        Log.d(TAG, "OnResume")
        if (internetCheck(this)) {
            getPosts()
            floatingActionButton.visibility = View.VISIBLE
        } else {
            getPostsOffline()
            floatingActionButton.visibility = View.GONE
        }
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

