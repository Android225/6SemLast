package com.example.a6semlast.fragments

import android.os.Bundle
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
import com.example.a6semlast.fragments.data.HolidayResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApiFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultTextView: TextView
    private lateinit var buttonRefresh: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    private val apiKey = "B36RxE6D0WvsRhX4rEdKyoONUUik2zuU"
    private val country = "RU"
    private val year = 2024

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        resultTextView = view.findViewById(R.id.resultTextView)
        buttonRefresh = view.findViewById(R.id.buttonRefresh)
        loadingProgressBar = view.findViewById(R.id.loading)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)

        setupSearchButton()

        fetchHolidays()

        return view
    }

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchHolidays(query)
            }
        }
    }

    private fun fetchHolidays() {
        loadingProgressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            val response = try {
                RetrofitInstance.api.getHolidays(apiKey, country, year, "") // Передаем пустую строку для параметра запроса
            } catch (e: Exception) {
                showError()
                null
            }

            response?.let { handleResponse(it) }
        }
    }


    private fun handleResponse(response: HolidayResponse) {
        loadingProgressBar.visibility = View.GONE
        if (response.meta.code == 200) {
            val holidays = response.response.holidays
            if (holidays.isNotEmpty()) {
                resultTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = HolidayAdapter(holidays)
            } else {
                showNoResults()
            }
        } else {
            showError()
        }
    }

    private fun searchHolidays(query: String) {
        loadingProgressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            val response = try {
                RetrofitInstance.api.getHolidays(apiKey, country, year, query)
            } catch (e: Exception) {
                showError()
                null
            }

            response?.let { handleResponse(it) }
        }
    }


    private fun showNoResults() {
        resultTextView.text = "No results found"
        resultTextView.visibility = View.VISIBLE
        buttonRefresh.visibility = View.GONE
    }

    private fun showError() {
        resultTextView.text = "Error loading data"
        resultTextView.visibility = View.VISIBLE
        buttonRefresh.visibility = View.VISIBLE
        buttonRefresh.setOnClickListener {
            fetchHolidays()
        }
    }
}
