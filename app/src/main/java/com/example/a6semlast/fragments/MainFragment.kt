package com.example.a6semlast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.a6semlast.R



class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bF1 = view.findViewById<Button>(R.id.buttonLogin)
        val bF2 = view.findViewById<Button>(R.id.buttonapi)
        val bF3 = view.findViewById<Button>(R.id.buttonnight)
        val controller = findNavController()
        bF1.setOnClickListener {controller.navigate(R.id.fragment13)}
        bF2.setOnClickListener {controller.navigate(R.id.apiFragment2)}
        bF3.setOnClickListener { controller.navigate(R.id.themeSwitcherFragment2) }
    }
}