package com.example.criminalintent.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.database.Crime
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.text.SimpleDateFormat
import java.util.*

//simply put, this is like model of item
class CrimeHolder(
    private val bindingMy: ListItemCrimeBinding
) : RecyclerView.ViewHolder(bindingMy.root) {
    fun bind(crime: Crime, onCrimeClicked : (crimeId: UUID) -> Unit) {
        bindingMy.apply {
            //Task 22.04.23_1: show date like: "Wednesday, May 11, 2022"
            val formatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val answer: String = formatter.format(crime.date)

            tvCrimeTitle.setText(crime.title)
            tvCrimeData.setText(answer)

            crimeSolved.visibility = if(crime.isSolved){
                View.VISIBLE
            } else {
                View.GONE
            }

            root.setOnClickListener {
                //Toast.makeText(root.context, "${crime.title} clicked!", Toast.LENGTH_SHORT).show()
                onCrimeClicked(crime.id)
            }
        }
    }
}

