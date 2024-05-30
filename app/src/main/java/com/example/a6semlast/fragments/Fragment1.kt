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


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Пример данных
        val tasks = listOf(
            Task("Task 1", "Description 1"),
            Task("Task 2", "Description 2"),
            Task("Task 3", "Description 3")
        )

        // Получаем ListView и устанавливаем адаптер
        val listViewTasks = view.findViewById<ListView>(R.id.listViewTasks)
        val adapter = TaskAdapter(requireContext(), tasks)
        listViewTasks.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
        val bF1 = view.findViewById<Button>(R.id.buttonHome)
        val bF2 = view.findViewById<Button>(R.id.buttonCalendar)
        val bF3 = view.findViewById<Button>(R.id.buttonSearch)
        val bF4 = view.findViewById<Button>(R.id.buttonAdd)
        val controller = findNavController()
        bF1.setOnClickListener {controller.navigate(R.id.fragment13)}
        bF2.setOnClickListener {controller.navigate(R.id.fragment23)}
        bF3.setOnClickListener {controller.navigate(R.id.loop_fragment2)}
        bF4.setOnClickListener {controller.navigate(R.id.addFragment)}

    }
}
