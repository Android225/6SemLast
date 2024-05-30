package com.example.a6semlast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat

class TaskAdapter(context: Context, private val tasks: List<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val task = tasks[position]

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val checkBoxCompleted = view.findViewById<CheckBox>(R.id.checkBoxCompleted)

        textViewTitle.text = task.title
        textViewDescription.text = task.description
        checkBoxCompleted.isChecked = task.completed

        // Set checkbox background color based on task priority
        checkBoxCompleted.setButtonDrawable(R.drawable.rounded_checkbox)

        return view
    }

    private fun getPriorityColor(priority: Int): Int {
        val colorResId = when (priority) {
            5 -> R.color.priority_4
            4 -> R.color.priority_3
            3 -> R.color.priority_2
            2 -> R.color.priority_1
            1 -> R.color.grayy
            6 -> R.color.priority_5
            else -> R.color.grayy
        }
        return colorResId
    }
}
