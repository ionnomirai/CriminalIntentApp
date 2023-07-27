package com.example.criminalintent.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.criminalintent.database.Crime
import com.example.criminalintent.repository.CrimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeDetailViewModel_TAG"

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val repository = CrimeRepository.get()

    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val crime
        get() = _crime.asStateFlow()


    // When viewmodel has created, at once get data from database,
    init {
        viewModelScope.launch {
            _crime.value = repository.getCrime(crimeId)
        }
    }

    // Function that containg lambda in the parameters (it is Crime's changes and return Crime ).
    // When we get Crime from lambda, we atomically update current stateflow value on the new custom value
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //if value != null, then we updateCrime (as current value)
        crime.value?.let { repository.updateCrime(it) } // it - it is value

        // it is my alternariv way, how avoid using GlobalScope
        /*        CoroutineScope(Dispatchers.IO).launch {
            crime.value?.let { repository.updateCrimeTest(it) }
        }*/
        Log.d(TAG, "onCleared")
    }

    // it is my alternariv way, how avoid using GlobalScope
    //suspend fun updateCrimeTest(crime: Crime) = repository.updateCrimeTest(crime)
}

class CrimeDetailViewModelFactory(
    val crimeId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}