package com.example.a6semlast.fragments

import ApiService
import MyAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a6semlast.R
import org.json.JSONArray
import org.json.JSONException

class ApiFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultTextView: TextView
    private lateinit var buttonRefresh: Button
    private lateinit var loading: View
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        resultTextView = view.findViewById(R.id.resultTextView)
        buttonRefresh = view.findViewById(R.id.buttonRefresh)
        loading = view.findViewById(R.id.loading)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService()
        adapter = MyAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        buttonRefresh.setOnClickListener {
            fetchData()
        }

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                // Если текст не введен, можно вывести сообщение об ошибке или выполнить другие действия
            }
        }

        fetchData() // Выполняем начальную загрузку данных
    }

    private fun fetchData() {
        showLoading(true)
        apiService.searchNameByDate("0505", "cs", object : ApiService.ApiCallback {
            override fun onSuccess(response: String) {
                Log.d("ApiFragment", "API Response (fetchData): $response")
                activity?.runOnUiThread {
                    showLoading(false)
                    try {
                        val jsonArray = JSONArray(response)
                        val list = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val name = jsonObject.getString("name")
                            Log.d("ApiFragment", "Parsed name (fetchData): $name")
                            list.add(name)
                        }
                        if (list.isEmpty()) {
                            showPlaceholder("No results found")
                        } else {
                            adapter.setData(list)
                        }
                    } catch (e: JSONException) {
                        Log.e("ApiFragment", "JSON Parsing error (fetchData): ${e.message}")
                        showPlaceholder("Error parsing data")
                    }
                }
            }

            override fun onError(error: String) {
                Log.e("ApiFragment", "API Error (fetchData): $error")
                activity?.runOnUiThread {
                    showLoading(false)
                    showPlaceholder(error)
                }
            }
        })
    }

    private fun performSearch(searchText: String) {
        showLoading(true)
        apiService.searchNameByDate(searchText, "cs", object : ApiService.ApiCallback {
            override fun onSuccess(response: String) {
                Log.d("ApiFragment", "API Response (performSearch): $response")
                activity?.runOnUiThread {
                    showLoading(false)
                    try {
                        val jsonArray = JSONArray(response)
                        val list = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val name = jsonObject.getString("name")
                            Log.d("ApiFragment", "Parsed name (performSearch): $name")
                            list.add(name)
                        }
                        if (list.isEmpty()) {
                            showPlaceholder("No results found")
                        } else {
                            adapter.setData(list)
                        }
                    } catch (e: JSONException) {
                        Log.e("ApiFragment", "JSON Parsing error (performSearch): ${e.message}")
                        showPlaceholder("Error parsing data")
                    }
                }
            }

            override fun onError(error: String) {
                Log.e("ApiFragment", "API Error (performSearch): $error")
                activity?.runOnUiThread {
                    showLoading(false)
                    showPlaceholder(error)
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        activity?.runOnUiThread {
            if (isLoading) {
                resultTextView.text = "Loading..."
                resultTextView.visibility = View.VISIBLE
                loading.visibility = View.VISIBLE
            } else {
                resultTextView.visibility = View.GONE
                loading.visibility = View.GONE
            }
        }
    }

    private fun showPlaceholder(message: String) {
        activity?.runOnUiThread {
            resultTextView.text = message
            resultTextView.visibility = View.VISIBLE
            buttonRefresh.visibility = View.VISIBLE
        }
    }
}
