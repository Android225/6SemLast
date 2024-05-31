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

class Fragment2 : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private val tasksReference = database.getReference("tasks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listViewTasks = view.findViewById<ListView>(R.id.listViewTasks)
        val tasksList = mutableListOf<Task>()
        val tasksAdapter = TaskAdapter(requireContext(), tasksList)
        listViewTasks.adapter = tasksAdapter

        tasksReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasksList.clear()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    task?.let { tasksList.add(it) }
                }
                tasksAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fragment2", "Database error: ${error.message}")
            }
        })

        val bF1 = view.findViewById<Button>(R.id.buttonHomeCal)
        val bF2 = view.findViewById<Button>(R.id.buttonCalendarCal)
        val bF3 = view.findViewById<Button>(R.id.buttonSearchCal)

        val controller = findNavController()
        bF1.setOnClickListener { controller.navigate(R.id.fragment13) }
        bF2.setOnClickListener { controller.navigate(R.id.fragment23) }
        bF3.setOnClickListener { controller.navigate(R.id.loop_fragment2) }
    }
}
