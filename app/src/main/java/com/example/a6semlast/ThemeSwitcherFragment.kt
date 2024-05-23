package com.example.a6semlast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a6semlast.App
import com.example.a6semlast.R
import com.google.android.material.switchmaterial.SwitchMaterial

class ThemeSwitcherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_switcher2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)

        // Set the switch state based on the current theme
        themeSwitcher.isChecked = (activity?.applicationContext as App).darkTheme

        // Set the switch change listener
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (activity?.applicationContext as App).switchTheme(isChecked)
        }
    }
}
