package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

const val VIEW_MODEL_TAG = "View_Model"
class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    private val crimeRepository = CrimeRepository.get()

/*    init {
        Log.d(VIEW_MODEL_TAG, "init starting")
        viewModelScope.launch {
            Log.d(VIEW_MODEL_TAG, "coroutine launched")

            crimes += loadCrime()

            Log.d(VIEW_MODEL_TAG, "Loading crimes finished")
        }
    }*/

     suspend fun loadCrime() : List<Crime>{
/*        val result = mutableListOf<Crime>()
         delay(4000L)
        for (i in 0 until 100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0
            )
            result.add(crime)
        }
        return result*/
         return crimeRepository.getCrimes()
    }
}