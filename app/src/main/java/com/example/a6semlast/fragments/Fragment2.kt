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

class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Пример данных
        val tasks = listOf(
            Task("Task A", "Description A"),
            Task("Task B", "Description B"),
            Task("Task C", "Description C")
        )

        // Получаем ListView и устанавливаем адаптер
        val listViewTasks2 = view.findViewById<ListView>(R.id.listViewTasks2)
        val adapter = TaskAdapter(requireContext(), tasks)
        listViewTasks2.adapter = adapter

        // Настраиваем кнопки
        val bHome = view.findViewById<Button>(R.id.buttonHomeCal)
        val bCalendar = view.findViewById<Button>(R.id.buttonCalendarCal)
        val bSearch = view.findViewById<Button>(R.id.buttonSearchCal)
        val controller = findNavController()
        bHome.setOnClickListener { controller.navigate(R.id.fragment13) }
        bCalendar.setOnClickListener { controller.navigate(R.id.fragment23) }
        bSearch.setOnClickListener { controller.navigate(R.id.loop_fragment2) }
    }
}
