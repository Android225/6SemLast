package com.example.a6semlast

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskAdapter(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    private val currentUserUid: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks/users/$currentUserUid/tasks")
    private val originalTasks: MutableList<Task> = mutableListOf()

    init {
        originalTasks.addAll(tasks)
    }

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

        checkBoxCompleted.setOnCheckedChangeListener(null)
        checkBoxCompleted.isChecked = task.completed

        checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            database.child(task.id).setValue(task)
        }

        return view
    }

    fun filter(text: String) {
        tasks.clear()
        if (TextUtils.isEmpty(text)) {
            tasks.addAll(originalTasks)
        } else {
            val filteredTasks = originalTasks.filter { task ->
                task.title.contains(text, ignoreCase = true) || task.description.contains(text, ignoreCase = true)
            }
            tasks.addAll(filteredTasks)
        }
        notifyDataSetChanged()
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format("%02d.%02d.%04d", day, month + 1, year)
    }
}
