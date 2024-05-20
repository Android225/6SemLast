package com.example.a6semlast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R
import com.example.a6semlast.Task
import com.example.a6semlast.TaskAdapter


class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1, container, false)
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
        val controller = findNavController()
        bF1.setOnClickListener {controller.navigate(R.id.fragment13)}
        bF2.setOnClickListener {controller.navigate(R.id.fragment23)}
        bF3.setOnClickListener {controller.navigate(R.id.loop_fragment2)}

    }
}