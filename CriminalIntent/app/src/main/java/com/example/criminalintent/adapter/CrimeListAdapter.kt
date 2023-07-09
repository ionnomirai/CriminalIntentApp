package com.example.criminalintent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.database.Crime
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.util.UUID

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (id: UUID) -> Unit
) : RecyclerView.Adapter<CrimeHolder>() {

    /*create an object CrimeHolder and pass into it a binding object ("an object of layout")*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    /*Fill the current ViewHolder (created in the onCreateViewHolder) with the information*/
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position] //the current position item in the list of crimes
        //set the data in the current ViewHolder
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount(): Int {
        return crimes.size
    }
}