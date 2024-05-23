package com.example.a6semlast.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a6semlast.R
import com.example.a6semlast.fragments.adapter.HolidayAdapter
import com.example.a6semlast.fragments.adapter.RetrofitInstance
import com.example.a6semlast.fragments.adapter.SearchHistoryAdapter
import com.example.a6semlast.fragments.data.Holiday
import com.example.a6semlast.fragments.data.HolidayResponse
import com.example.a6semlast.fragments.data.Meta
import com.example.a6semlast.fragments.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ApiFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchResultsRecyclerView: RecyclerView // Добавляем переменную
    private lateinit var resultTextView: TextView
    private lateinit var buttonRefresh: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    private lateinit var allHolidays: List<Holiday>
    private val apiKey = "B36RxE6D0WvsRhX4rEdKyoONUUik2zuU"
    private val country = "RU"
    private val year = 2024

    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        resultTextView = view.findViewById(R.id.resultTextView)
        buttonRefresh = view.findViewById(R.id.buttonRefresh)
        loadingProgressBar = view.findViewById(R.id.loading)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        searchHistoryRecyclerView = view.findViewById(R.id.RecyclerHistory)

        Log.d("ApiFragment", "onCreateView: view inflated")

        setupSearchButton()
        setupSearchHistory()

        fetchHolidays()

        return view
    }

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchHolidays(query)
                addSearchQuery(query)
                updateSearchHistory()
            }
        }
    }

    private fun fetchHolidays() {
        loadingProgressBar.visibility = View.VISIBLE // Показываем ProgressBar перед выполнением запроса
        GlobalScope.launch(Dispatchers.Main) {
            val response = try {
                RetrofitInstance.api.getHolidays(apiKey, country, year, "")
            } catch (e: Exception) {
                showError()
                null
            }

            response?.let {
                allHolidays = it.response.holidays // Сохраняем полный список праздников
                handleResponse(it)
            }

            loadingProgressBar.visibility = View.GONE // Скрываем ProgressBar после выполнения запроса
        }
    }

    private fun handleResponse(response: HolidayResponse, isSearch: Boolean = false) {
        loadingProgressBar.visibility = View.GONE
        if (response.meta.code == 200) {
            val holidays = response.response.holidays
            if (holidays.isNotEmpty()) {
                resultTextView.visibility = View.GONE
                if (isSearch) {
                    searchResultsRecyclerView.visibility = View.VISIBLE
                    searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    searchResultsRecyclerView.adapter = HolidayAdapter(holidays)
                } else {
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = HolidayAdapter(holidays)
                }
            } else {
                showNoResults()
            }
        } else {
            showError()
        }
    }

    private fun searchHolidays(query: String) {
        loadingProgressBar.visibility = View.VISIBLE // Показываем ProgressBar перед выполнением запроса
        GlobalScope.launch(Dispatchers.Main) {
            val filteredHolidays = allHolidays.filter { holiday ->
                holiday.name.contains(query, ignoreCase = true)
            }

            // Создаем объект Meta с кодом 200
            val meta = Meta(200)

            // Создаем объект HolidayResponse, передавая объекты Meta и Response
            val holidayResponse = HolidayResponse(meta, Response(filteredHolidays))

            // Передаем результаты поиска для обработки
            handleResponse(holidayResponse, isSearch = true)

            loadingProgressBar.visibility = View.GONE // Скрываем ProgressBar после выполнения запроса
        }
    }

    private fun showNoResults() {
        resultTextView.text = "No results found"
        resultTextView.visibility = View.VISIBLE
        buttonRefresh.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchResultsRecyclerView.visibility = View.GONE
    }

    private fun showError() {
        resultTextView.text = "Error loading data"
        resultTextView.visibility = View.VISIBLE
        buttonRefresh.visibility = View.VISIBLE
        buttonRefresh.setOnClickListener {
            fetchHolidays()
        }
        recyclerView.visibility = View.GONE
        searchResultsRecyclerView.visibility = View.GONE
    }

    private val PREFS_NAME = "search_prefs"
    private val PREFS_KEY = "search_history"

    private fun getSearchHistory(): MutableList<String> {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        val historyJson = prefs.getString(PREFS_KEY, null)
        return if (historyJson != null) {
            val type = object : TypeToken<MutableList<String>>() {}.type
            Gson().fromJson(historyJson, type)
        } else {
            mutableListOf()
        }
    }

    private fun addSearchQuery(query: String) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        val history = getSearchHistory()

        // Удаляем существующий элемент, если он уже есть в истории
        if (history.contains(query)) {
            history.remove(query)
        }

        // Добавляем новый элемент в начало списка
        history.add(0, query)

        // Обрезаем список до 10 элементов
        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        // Сохраняем историю в SharedPreferences
        val historyJson = Gson().toJson(history)
        editor.putString(PREFS_KEY, historyJson)
        editor.apply()
    }

    private fun clearSearchHistory() {
        Log.d("ApiFragment", "clearSearchHistory: Clearing search history") // Добавлено отладочное сообщение
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.remove(PREFS_KEY)
        editor.apply()
        Log.d("ApiFragment", "clearSearchHistory: Search history cleared") // Отладочное сообщение
        updateSearchHistory() // Обновляем адаптер после очистки
    }

    private fun setupSearchHistory() {
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryAdapter = SearchHistoryAdapter(getSearchHistory()) { query ->
            searchEditText.setText(query)
            searchHolidays(query)
        }
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        val clearHistoryButton: Button? = view?.findViewById(R.id.buttonClearHistory)
        clearHistoryButton?.setOnClickListener {
            Log.d("ApiFragment", "setupSearchHistory: Clear History button clicked") // Отладочное сообщение
            clearSearchHistory()
        }
    }

    private fun updateSearchHistory() {
        val history = getSearchHistory()
        searchHistoryAdapter.updateData(history)
        Log.d("ApiFragment", "updateSearchHistory: Search history updated: $history") // Отладочное сообщение
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Другой код, если есть

        val clearHistoryButton: Button = view.findViewById(R.id.buttonClearHistory)
        clearHistoryButton.setOnClickListener {
            Log.d("ApiFragment", "Clear History button clicked") // Отладочное сообщение
            clearSearchHistory()
        }

        // Добавляем TextWatcher к EditText для отслеживания изменений в тексте
        searchEditText.addTextChangedListener(textWatcher)
    }


    private val handler = Handler()

    private val searchRunnable = Runnable {
        val query = searchEditText.text.toString()
        if (query.isNotEmpty()) {
            searchHolidays(query)
            addSearchQuery(query)
            updateSearchHistory()
        }
    }

    private val delayMilliseconds = 2000L // Задержка в 2 секунды

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Ничего не делаем
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Ничего не делаем
        }

        override fun afterTextChanged(s: Editable?) {
            handler.removeCallbacks(searchRunnable) // Очищаем предыдущий запланированный поиск
            handler.postDelayed(searchRunnable, delayMilliseconds) // Планируем новый поиск через 2 секунды
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Удаляем TextWatcher при уничтожении представления (фрагмента)
        searchEditText.removeTextChangedListener(textWatcher)
    }
}
