package com.example.criminalintent.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate // Date transform into calendar and apply

        val initialYear = calendar.get(Calendar.YEAR) // current year
        val initialMonth = calendar.get(Calendar.MONTH) // current month
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH) // current day

        // listener that handle user's date selection
        // here year, month and day depend on the date's selection by the user
        // view from lambda is for the DatePicker the result is coming from. So, it is mean,
        //      that this listener can be used for different DatePickerDialogs, and it is
        //      still works great.
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val resultDate = GregorianCalendar(year, month, dayOfMonth).time

            setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
        }

        return DatePickerDialog(
            requireContext(),
            listener,
            initialYear, // selected year
            initialMonth, // selected month
            initialDay // selected day
        )
    }

    companion object{
        // This is constant needed to communicate between this FragmentDialog, and CrimeDetailFragment
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_RESULT"
    }
}