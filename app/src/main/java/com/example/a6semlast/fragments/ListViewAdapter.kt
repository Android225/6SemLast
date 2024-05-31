package com.example.a6semlast

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskAdapter(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val task = tasks[position]

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
        val checkBoxCompleted = view.findViewById<CheckBox>(R.id.checkBoxCompleted)

        textViewTitle.text = task.title
        textViewDescription.text = task.description
        textViewDate.text = formatDate(task.day, task.month, task.year)

        // Установка состояния чекбокса без слушателя
        checkBoxCompleted.setOnCheckedChangeListener(null)
        checkBoxCompleted.isChecked = task.completed

        checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d("TaskAdapter", "Attempting to remove task with ID: ${task.id}")
                database.child(task.id).removeValue().addOnCompleteListener { taskRemoval ->
                    if (taskRemoval.isSuccessful) {
                        Log.d("TaskAdapter", "Successfully removed task with ID: ${task.id}")
                        tasks.remove(task)
                        notifyDataSetChanged()
                    } else {
                        Log.e("TaskAdapter", "Failed to remove task with ID: ${task.id}")
                        checkBoxCompleted.isChecked = false // Восстановление состояния чекбокса в случае ошибки
                    }
                }
            }
        }

        return view
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format("%02d.%02d.%04d", day, month + 1, year)
    }
}
