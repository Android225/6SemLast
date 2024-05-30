package com.example.a6semlast

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.a6semlast.R
import com.google.firebase.database.*

class TaskAdapter(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    private val database = FirebaseDatabase.getInstance()
    private val tasksReference = database.getReference("tasks")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val task = tasks[position]

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate) // Добавлено для даты
        val checkBoxCompleted = view.findViewById<CheckBox>(R.id.checkBoxCompleted)

        textViewTitle.text = task.title
        textViewDescription.text = task.description
        textViewDate.text = formatDate(task.day, task.month, task.year) // Устанавливаем дату

        // Log the priority
        Log.d("TaskAdapter", "Task Priority: ${task.priority}")

        // Set checkbox background color based on task priority
        checkBoxCompleted.setBackgroundResource(R.drawable.rounded_checkbox)

        // Set up listener for checkbox click event
        checkBoxCompleted.setOnClickListener {
            // Remove the task from the database
            tasksReference.child(task.id).removeValue()
            // Remove the task from the list
            tasks.removeAt(position)
            notifyDataSetChanged() // Notify the adapter that the data set has changed
        }

        return view
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format("%02d.%02d.%04d", day, month + 1, year) // Форматируем дату
    }
 }
