package com.example.criminalintent.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.database.Crime
import com.example.criminalintent.databinding.FragmentCrimeBinding
import java.util.*

private const val TAG = "CrimeDetailFragment_TAG"

class CrimeDetailFragment : Fragment(){
    private lateinit var crime: Crime

    /* Variable that holds any data that come here from another fragments
    *  (in our case, it is UUID from CrimeListFragment*/
    private val argsMy : CrimeDetailFragmentArgs by navArgs()

    private var _binding: FragmentCrimeBinding? = null //instance of the layout
    //checkNotNull(value) if value equals null -> than throe exception, otherwise return value
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot accesss binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Fragment", "onCreateFragment")
        crime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = false
        )
        Log.d("Fragment", "Crime id: ${crime.id}")
    }

    //in that function we create instance of the layout and poss it to the host
    override fun onCreateView(
        inflater: LayoutInflater,//creates an instance of the layout
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Fragment", "onCreateView")
        //layoutInflater it is equals getLayoutInflater
        //question! Can we call inflater instead getLayoutInflater?
        //purpose: create an instance of the layout
        _binding = FragmentCrimeBinding.inflate(layoutInflater, container, false)
        return binding.root //go to the root element of views that contains all other views
    }

    //in that function we initialise variables and setting listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Fragment", "onViewCreated")
        binding.apply {
            /*it is filling the title field in crime object, at the same time that the user enters
            this information into EditText* field*/
            etCrimeTitle.doOnTextChanged { text, _, _, _ ->
                crime = crime.copy(title = text.toString())
            }

            /*temporary blocked. General sense it is to set a text into the button like today date,
            and set this information to the same field in the crime obj. It is will be reflect
            when we added this crime */
            bCrimeDate.apply {
                text = crime.date.toString()
                isEnabled = false
            }

            /**/
            cbCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crime = crime.copy(isSolved = isChecked)
            }
        }

        Log.d(TAG, "arguments from another fragments: ${argsMy.crimeId}")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("Fragment", "onDestroyView")
    }
}