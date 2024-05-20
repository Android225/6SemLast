package com.example.a6semlast.fragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a6semlast.R
import com.example.a6semlast.fragments.data.Holiday

class HolidayAdapter(private val holidays: List<Holiday>) : RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder>() {

    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_holiday, parent, false)
        return HolidayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val holiday = holidays[position]
        holder.nameTextView.text = holiday.name
        holder.descriptionTextView.text = holiday.description
        holder.dateTextView.text = holiday.date.iso
    }

    override fun getItemCount(): Int {
        return holidays.size
    }

}

