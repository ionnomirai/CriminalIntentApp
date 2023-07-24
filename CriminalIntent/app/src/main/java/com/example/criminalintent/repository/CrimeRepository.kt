package com.example.criminalintent.repository

import android.content.Context
import androidx.room.Room
import com.example.criminalintent.database.Crime
import com.example.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

/* - using GlobalScope. I think this is not good idea. In book that I read, it needs to do
*    an update that will call in ViewModel and continue to do it even if ViewModel destructs.
*    I think we can work in one ViewModel, that are "attached" to activity and all fragments.
*    Yes, it is will create new instances for all fragments, but they also will be destruct with
*    fragments. And it is more safely.*/
class CrimeRepository private constructor(
    context: Context,
    private val coroitoneScope: CoroutineScope = GlobalScope
) {
    private val DATABASE_NAME = "crime-database"
    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)
    fun updateCrime(crime: Crime) {
        coroitoneScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}