package com.example.criminalintent.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.criminalintent.R
import com.example.criminalintent.adapter.CrimeListAdapter
import com.example.criminalintent.viewModel.CrimeListViewModel
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch


class CrimeListFragment : Fragment() {
    private val TAG = "CrimeListFragment"
    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        // set a LayoutManager for RecyclerView.
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes) {currentCrimeUUID ->
                        /*we can write like this, because the lambda is last parameter*/
                        /*it is link of action, that describe where need to move*/
                        //findNavController().navigate(R.id.show_crime_detail)
                        findNavController().navigate(
                            CrimeListFragmentDirections.showCrimeDetail(currentCrimeUUID)
                        )
                        Log.d(TAG, "after showCrimeDetail")
                    }
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
        Log.d(TAG, "called onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "called onDestroy")
    }
}