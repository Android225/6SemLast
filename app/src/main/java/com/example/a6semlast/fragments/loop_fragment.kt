package com.example.a6semlast.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R
import com.example.a6semlast.Task
import com.example.a6semlast.TaskAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class loop_fragment : Fragment() {
    private lateinit var editTextSearch: EditText
    private lateinit var listViewSearchResults: ListView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var userTasksReference: DatabaseReference
    private val originalTasks: MutableList<Task> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loop_fragment, container, false)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        listViewSearchResults = view.findViewById(R.id.listViewSearchResults
        )

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        userTasksReference = FirebaseDatabase.getInstance().getReference("tasks/users/$currentUserUid/tasks")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера
        taskAdapter = TaskAdapter(requireContext(), mutableListOf())
        listViewSearchResults.adapter = taskAdapter

        // Добавление обработчика текстового поля для поиска
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterTasks(s.toString()) // Фильтрация задач по тексту
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Получение данных из Firebase и отображение их в списке
        userTasksReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                originalTasks.clear()
                for (snapshot in dataSnapshot.children) {
                    val task = snapshot.getValue(Task::class.java)
                    task?.let {
                        originalTasks.add(it)
                    }
                }
                filterTasks(editTextSearch.text.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                // Ошибка при загрузке данных из Firebase
            }
        })

        // Код для кнопок и других действий на вашем фрагменте
        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        val buttonHomeLp = view.findViewById<Button>(R.id.buttonHomeLp)
        val buttonCalendarLp = view.findViewById<Button>(R.id.buttonCalendarLp)
        val buttonSearchLp = view.findViewById<Button>(R.id.buttonSearchLp)
        val buttonClear = view.findViewById<Button>(R.id.buttonClear)

        buttonClear.setOnClickListener {
            editTextSearch.text = null
            // Скрыть клавиатуру
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_loop_fragment2_to_fragment13)
        }

        buttonHomeLp.setOnClickListener {
            findNavController().navigate(R.id.fragment13)
        }
        buttonCalendarLp.setOnClickListener {
            findNavController().navigate(R.id.fragment23)
        }
        buttonSearchLp.setOnClickListener {
            findNavController().navigate(R.id.loop_fragment2)
        }
    }

    private fun filterTasks(text: String) {
        val filteredTasks = originalTasks.filter { task ->
            task.title.contains(text, ignoreCase = true) || task.description.contains(text, ignoreCase = true)
        }
        taskAdapter.clear()
        taskAdapter.addAll(filteredTasks)
    }
}