package com.example.criminalintent

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding

//simply put, this is like model of item
class CrimeHolder(
    private val bindingMy: ListItemCrimeBinding
) : RecyclerView.ViewHolder(bindingMy.root) {
    fun bind(crime: Crime) {
        bindingMy.apply {
            tvCrimeTitle.setText(crime.title)
            tvCrimeData.setText(crime.date.toString())

            root.setOnClickListener {
                Toast.makeText(root.context, "${crime.title} clicked!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}