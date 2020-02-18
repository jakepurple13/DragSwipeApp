package com.programmerbox.dragswipeapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.programmerbox.dragswipe.DragSwipeAdapter
import kotlinx.android.synthetic.main.ds_layout.view.*

class DSAdapter(list: ArrayList<String>, var context: Context) :
    DragSwipeAdapter<String, ViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.ds_layout,
                parent,
                false
            )
        )
    }

    override fun String.onBind(holder: ViewHolder, position: Int) {

    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.setOnClickListener {
            Toast.makeText(context, "I was clicked!", Toast.LENGTH_SHORT).show()
        }

        holder.button.text = "Click me!"

        holder.text.text =
            "$position with ${dataList[position]}: Random Text to show how awesome this library is!"

        holder.button.setOnTouchListener(null)
        holder.text.setOnTouchListener(null)

        if (helper != null) {
            holder.button.text = "Drag with Me!\nAnd still click!"

            holder.text.text =
                "$position with ${dataList[position]}: Random Text to show how awesome this library is!\nSwipe with me!"

            holder.button.setOnTouchListener { _, _ ->
                helper!!.startDrag(holder)
                false
            }

            holder.text.setOnTouchListener { _, _ ->
                helper!!.startSwipe(holder)
                false
            }
        }

    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.textView!!
    val button: Button = view.button!!
}
