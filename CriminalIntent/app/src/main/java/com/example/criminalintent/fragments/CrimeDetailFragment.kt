package com.example.criminalintent.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.System.DATE_FORMAT
import android.provider.Settings.System.TIME_12_24
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.R
import com.example.criminalintent.database.Crime
import com.example.criminalintent.databinding.FragmentCrimeBinding
import com.example.criminalintent.viewModel.CrimeDetailViewModel
import com.example.criminalintent.viewModel.CrimeDetailViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.Closeable
import java.util.*

private const val TAG = "CrimeDetailFragment_TAG"

class CrimeDetailFragment : Fragment() {

    /* Variable that holds any data that come here from another fragments
    *  (in our case, it is UUID from CrimeListFragment*/
    private val argsMy: CrimeDetailFragmentArgs by navArgs()

    private var _binding: FragmentCrimeBinding? = null //instance of the layout

    private var currentCrime : Crime? = null

    //checkNotNull(value) if value equals null -> than throe exception, otherwise return value
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot accesss binding because it is null. Is the view visible?"
        }

    private val viewModelDetails: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(argsMy.crimeId)
    }

    private val menuProvider = object : MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_crime_edit, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                R.id.icDelete -> {
                    viewLifecycleOwner.lifecycleScope.launch{
                        currentCrime?.let { viewModelDetails.deleteCrime(it) }
                        findNavController().navigate(CrimeDetailFragmentDirections.actionCrimeDetailFragmentToNavGraph())
                    }
                    return true
                }
                else -> return false
            }
        }
    }

    // object that get some information (according contract) from Contacts App in this case
    // We get only uri, than we need manually parse it in readable dataa
    private val selectSuspect = registerForActivityResult(ActivityResultContracts.PickContact()){
        uri: Uri? ->
        uri?.let { parseContactSelection(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    //in that function we create instance of the layout and poss it to the host
    override fun onCreateView(
        inflater: LayoutInflater,//creates an instance of the layout
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        //layoutInflater it is equals getLayoutInflater
        //question! Can we call inflater instead getLayoutInflater?
        //purpose: create an instance of the layout
        _binding = FragmentCrimeBinding.inflate(layoutInflater, container, false)
        return binding.root //go to the root element of views that contains all other views
    }

    //in that function we initialise variables and setting listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        //Log.d(TAG, "${viewModelDetails.crime.value?.id}")

        // added action in app bar
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)

        binding.apply {
            // callback responsible for the back button behavior
            val callbackAction2 = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "custom back press")
                    if (etCrimeTitle.text.toString().isEmpty()) {
                        //Toast.makeText(activity, "You press BACK version 2", Toast.LENGTH_SHORT).show()
                        Snackbar.make(root, "The title can not be empty", Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        findNavController().navigate(
                            CrimeDetailFragmentDirections.actionCrimeDetailFragmentToNavGraph()
                        )
                    }
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                callbackAction2
            )


            /*it is filling the title field in crime object, at the same time that the user enters
            this information into EditText* field*/
            etCrimeTitle.doOnTextChanged { text, _, _, _ ->
                viewModelDetails.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString()) // it is equals like "return oldCrime.copy..."
                }
            }

            cbCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                viewModelDetails.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked) // it is equals like "return oldCrime.copy..."
                }
            }

            // button crimeSuspect
            // User will move to Contacts App --> pick one person from a list --> and will come back here
            crimeSuspect.setOnClickListener {
                /* as we launch a Contacts App, it is not demant any input, so we can fill input
                * field like 'null'*/
                selectSuspect.launch(null)
            }
        }

        // updating data on the screen
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelDetails.crime.collect { crime ->
                    Log.d(TAG, "Crime changed")
                    crime?.let {
                        currentCrime = it // save temp Crime for sync between Date and Time change. Look at fun createDateTime
                        updateUI(it) // pass current Crime to update UI
                    }
                }
            }
        }

        Log.d(TAG, "arguments from another fragments: ${argsMy.crimeId}")

        // get date from fragment
        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->

            // bundle.getSerializable is deprecated for API 33+. For this case, I create one more way
            val newDate =
                if (Build.VERSION.SDK_INT >= 33)
                    bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE, Date::class.java)
                        ?: Date()
                else
                    bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date

            val timeCrime = Calendar.getInstance()
            timeCrime.time = currentCrime?.date ?: Date()
            createDateTime(newDate, timeCrime)
            // it is old original way, to change date without time. (And even we change time, and then date - it deleted time)
            /*            viewModelDetails.updateCrime {
                            it.copy(date = newDate)
                        }*/
        }

        // get time from fragment
        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
            val newTime =
                if (Build.VERSION.SDK_INT >= 33) {
                    bundle.getSerializable(
                        TimePickerFragment.BUNDLE_KEY_TIME,
                        Calendar::class.java
                    ) ?: Calendar.getInstance()
                } else {
                    bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Calendar
                }

            createDateTime(currentCrime?.date ?: Date(), newTime)
        }
    }

    // Function that check (according with DB), do these UI fields need change or not --> and update them
    private fun updateUI(crime: Crime) {
        binding.apply {
            if (etCrimeTitle.text.toString() != crime.title) {
                etCrimeTitle.setText(crime.title)
            }
            bCrimeDate.text = crime.date.toString()
            cbCrimeSolved.isChecked = crime.isSolved

            //when press button with date, it is trigger to move to fragment with datePicker
            bCrimeDate.setOnClickListener {
                // in params of selectDate, we pass a date of current Crime (from DB)
                findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
            }

            // button that change the time of crime
            bChangeTime.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectTime(crime.date))
            }

            // button that was create implicit intent, call another app and send message to it
            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject)
                    )
                }
                //creating chooser
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    // title that will be shown
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }

            // if there is no suspect, than show default title in the button text place
            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }
        }
    }

    // my custom function
    // crime - it is current crime
    // date - it is changed date from another fragment
    // This function combine date and time, then update the crime`s date
    private fun createDateTime(date: Date, time: Calendar){
        val dateComplete = Calendar.getInstance()
        val tempDate = Calendar.getInstance()
        tempDate.time = date

        dateComplete.apply {
            set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, time.get(Calendar.MINUTE))
            set(Calendar.YEAR, tempDate.get(Calendar.YEAR))
            set(Calendar.MONTH, tempDate.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, tempDate.get(Calendar.DAY_OF_MONTH))
        }

        viewModelDetails.updateCrime {
            it.copy(date = dateComplete.time)
        }
    }

    private fun getCrimeReport(crime: Crime) : String {
        val solvedString = if(crime.isSolved){
            getString(R.string.crime_report_solved)
        }
        else{
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(TIME_12_24, crime.date).toString()

        val suspectText = if (crime.suspect.isBlank()){
            getString(R.string.crime_report_no_suspect)
        }
        else{
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspectText)
    }

    private fun parseContactSelection(contactUri: Uri){
        // Here we only create an array with one value inside - display_name
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        /* this statement do: ContentResolver sends request (with Uri) to contentProvider,
        * and receive back Cursor object, that contain "table" with information. In this case,
        * we receive a table with only one column (because in second parameter we put the
        * querryFields, that contain array with only one name of column).
        *
        *   here in query():
        * - uri - contactUri - address resource (data) what we need
        *
        * - projection - queryFields - it is a list of which columns to return. Passing null
        *      will return all columns, which is inefficient.
        *
        * - selection -A filter declaring which rows to return, formatted as an SQL WHERE
        *      clause (excluding the WHERE itself). Passing null will return all rows
        *       for the given URI.
        *
        * - selectionArgs - ??? End on 524
        *
        * - sortOrder - How to order the rows, formatted as an SQL ORDER BY clause
        *       (excluding the ORDER BY itself). Passing null will use the default
        *       sort order, which may be unordered.*/
        val  queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        /*! we can use "use", because Cursor implement Closeable Interface
        * ! "use" can safety close the resource
        * */
        queryCursor?.use {cursor ->
            if(cursor.moveToFirst()){
                //delete this line after all
                val colNames = cursor.columnNames
                /*pull the data from this line on "0 position" column*/
                val suspect = cursor.getString(0)
                viewModelDetails.updateCrime {oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
                for (i in colNames){
                    Log.d(TAG, "$${i}")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "called onDestroy")
    }
}