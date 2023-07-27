package com.example.criminalintent.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.Date

class TimePickerFragment : DialogFragment() {

    private val TAG ="TimePickerFragment_TAG"
    private val args : TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeTime

        val hourCur = calendar.get(Calendar.HOUR_OF_DAY)
        val minutesCur =calendar.get(Calendar.MINUTE)

        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val calendarResult = Calendar.getInstance()
            calendarResult.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarResult.set(Calendar.MINUTE, minute)
            //val dateResult = calendarResult.time

            setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to calendarResult))
        }

        return TimePickerDialog(requireContext(), listener, hourCur, minutesCur, true)
    }

    companion object{
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}