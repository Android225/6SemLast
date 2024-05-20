package com.example.a6semlast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class TaskAdapter(context: Context, private val tasks: List<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val task = tasks[position]

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        textViewTitle.text = task.title
        textViewDescription.text = task.description
        imageView.setImageResource(R.drawable.loop1) // Установка изображения

        return view
    }
}
