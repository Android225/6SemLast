package com.example.a6semlast.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R
class loop_fragment : Fragment() {
    private lateinit var editTextSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loop_fragment, container, false)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        val buttonHomeLp = view.findViewById<Button>(R.id.buttonHomeLp)
        val buttonCalendarLp = view.findViewById<Button>(R.id.buttonCalendarLp)
        val buttonSearchLp = view.findViewById<Button>(R.id.buttonSearchLp)
        val buttonClear = view.findViewById<Button>(R.id.buttonClear)

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Показать кнопку "Очистить", если текст в поле ввода не пустой
                buttonClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchQuery", editTextSearch.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString("searchQuery")?.let { searchQuery ->
            editTextSearch.setText(searchQuery)
        }
    }
}
