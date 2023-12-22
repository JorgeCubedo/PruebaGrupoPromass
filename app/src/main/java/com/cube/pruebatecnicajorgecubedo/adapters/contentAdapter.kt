package com.cube.pruebatecnicajorgecubedo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cube.pruebatecnicajorgecubedo.R

class ContentAdapter(private val dataSet: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        var TAG = "ContentAdapter"
        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.text_content)

        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.content_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]

        viewHolder.itemView.setOnClickListener {
            Toast.makeText(context, "Clicked " + (position + 1), Toast.LENGTH_LONG).show()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}