package com.example.a6semlast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R
import com.example.a6semlast.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var seekBarPriority: SeekBar

    private val currentUserUid: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private val database = FirebaseDatabase.getInstance()
    private val userTasksReference = database.getReference("tasks/users/$currentUserUid/tasks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bF1 = view.findViewById<Button>(R.id.buttonbackToFR1)
        val controller = findNavController()
        bF1.setOnClickListener { controller.navigate(R.id.fragment13) }

        // Инициализация полей ввода
        titleEditText = view.findViewById(R.id.editTextTitle)
        descriptionEditText = view.findViewById(R.id.editTextDescription)
        datePicker = view.findViewById(R.id.datePicker)
        seekBarPriority = view.findViewById(R.id.seekBarPriority)

        // Обработка нажатия на кнопку "Добавить задачу"
        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            // Получаем значения из полей ввода
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val priority = seekBarPriority.progress + 1

            // Создаем новую задачу
            val newTaskId = userTasksReference.push().key ?: ""
            val newTask = Task(
                newTaskId,
                title,
                description,
                year,
                month,
                day,
                priority
            )

            // Добавляем задачу в базу данных Firebase
            userTasksReference.child(newTaskId).setValue(newTask)

            // Очищаем поля ввода
            clearFields()
        }
    }

    private fun clearFields() {
        titleEditText.text.clear()
        descriptionEditText.text.clear()
        // Сбросить дату на текущую
        val calendar = Calendar.getInstance()
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        // Сбросить приоритет на средний
        seekBarPriority.progress = 2
    }
}
