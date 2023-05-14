package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

const val VIEW_MODEL_TAG = "View_Model"
class CrimeListViewModel : ViewModel() {
    // get a link to an object of CrimeRepositary (Singleton)
    private val crimeRepository = CrimeRepository.get()

    // Starage in ViewModel for data from database.
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())

    /* This is public access to a MutableStateFlow, but only for read.
       When we call "crimes" we are redirection to the "private _crimes" (read). This _crimes
       it is a storage for data from database, and it is constantly filled data from DB (init block).
       Even more, when we rotate the screen, viewmodel save this staroage, as opposed to simple Flow.
    */
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        viewModelScope.launch {
            crimeRepository.getCrimes().collect{
                _crimes.value = it
            }
        }
    }
}