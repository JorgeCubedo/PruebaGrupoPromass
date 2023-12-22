package com.cube.pruebatecnicajorgecubedo.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cube.Entry
import com.cube.pruebatecnicajorgecubedo.EntryActivity
import com.cube.pruebatecnicajorgecubedo.R

class MyAdapter (private val entryList : List<Entry>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val TAG = MyAdapter::class.simpleName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.content_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {

        val currentItem = entryList[position]
        holder.content.text = currentItem.content

        holder.content.setOnClickListener { v ->
            Log.d(TAG, currentItem.id + " - " + currentItem.author)
            val intent = Intent(context, EntryActivity::class.java).apply {
                putExtra("EntryId", currentItem.id)
                putExtra("EntryTitle", currentItem.title)
                putExtra("EntryAuthor", currentItem.author)
                putExtra("EntryContent", currentItem.content)
                putExtra("EntryDate", currentItem.postedDate)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return entryList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val content : TextView = itemView.findViewById(R.id.text_content)

    }
}