package com.example.a6semlast.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        val bF1 = view.findViewById<Button>(R.id.btnGOSTART)
        val controller = findNavController()
        bF1.setOnClickListener {controller.navigate(R.id.log_in_fragment)}
        // Создаем Handler для выполнения действия с задержкой
        Handler(Looper.getMainLooper()).postDelayed({
            // Получаем контроллер навигации
            val navController = findNavController()
            // Переходим на фрагмент log_in_fragment
            navController.navigate(R.id.log_in_fragment)
        }, 100) // Задержка 500 миллисекунд (0.5 секунды)
    }
}

