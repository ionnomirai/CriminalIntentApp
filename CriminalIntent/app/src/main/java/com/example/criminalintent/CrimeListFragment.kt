package com.example.criminalintent

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
    get() = checkNotNull(_binding){
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                Log.d(TAG, "coroutine started")
                val crimes = crimeListViewModel.loadCrime()
                binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes)
                Log.d(TAG, "coroutine ended")

                //моя реализация - рабочая - потом удалить (а 54-57 закоментировать)
                //в во viewModels наоборот раскоментировать закоментированное, и наоборот закоментировать
                //то что аналогичный код
/*                Log.d(TAG, "coroutine started")
                binding.crimeRecyclerView.adapter = if (crimeListViewModel.crimes.size > 0){
                    Log.d(TAG, "crimes used ${crimeListViewModel.crimes.size}")
                    CrimeListAdapter(crimeListViewModel.crimes)
                } else {
                    Log.d(TAG, "crimes load")
                    crimeListViewModel.crimes += crimeListViewModel.loadCrime()
                    CrimeListAdapter(crimeListViewModel.crimes)
                }
                Log.d(TAG, "coroutine ended")*/
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