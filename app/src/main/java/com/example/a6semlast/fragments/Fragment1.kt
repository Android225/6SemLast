package com.example.a6semlast.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R
import com.example.a6semlast.Task
import com.example.a6semlast.TaskAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Fragment1 : Fragment() {

    private lateinit var listView: ListView
    private lateinit var tasksList: ArrayList<Task>
    private lateinit var adapter: TaskAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment1, container, false)

        listView = view.findViewById(R.id.listViewTasks)
        tasksList = ArrayList()
        adapter = TaskAdapter(requireContext(), tasksList)
        listView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("tasks")

        loadTasksForToday()

        return view
    }

    private fun loadTasksForToday() {
        val currentDate = getCurrentDate()
        Log.d("Fragment1", "Loading tasks for today: $currentDate")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasksList.clear()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        val taskDate = formatDate(task.day, task.month, task.year)
                        if (taskDate == currentDate) {
                            tasksList.add(task)
                        }
                    }
                }
                tasksList.sortWith(compareByDescending { it.priority })
                adapter.notifyDataSetChanged()
                Log.d("Fragment1", "Tasks loaded: ${tasksList.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fragment1", "Database error: ${error.message}")
            }
        })
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format("%02d.%02d.%04d", day, month + 1, year)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bF1 = view.findViewById<Button>(R.id.buttonHome)
        val bF2 = view.findViewById<Button>(R.id.buttonCalendar)
        val bF3 = view.findViewById<Button>(R.id.buttonSearch)
        val bF4 = view.findViewById<Button>(R.id.buttonAdd)
        val controller = findNavController()
        bF1.setOnClickListener { controller.navigate(R.id.fragment13) }
        bF2.setOnClickListener { controller.navigate(R.id.fragment23) }
        bF3.setOnClickListener { controller.navigate(R.id.loop_fragment2) }
        bF4.setOnClickListener { controller.navigate(R.id.addFragment) }
    }
}
