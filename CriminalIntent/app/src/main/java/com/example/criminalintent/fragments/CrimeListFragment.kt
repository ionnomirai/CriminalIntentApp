package com.example.criminalintent.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.criminalintent.R
import com.example.criminalintent.adapter.CrimeListAdapter
import com.example.criminalintent.database.Crime
import com.example.criminalintent.viewModel.CrimeListViewModel
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class CrimeListFragment : Fragment() {
    private val TAG = "CrimeListFragment"
    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    // implements of menu interface - this object PROVIDE creating menu
    val menuProvider = object : MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_crime_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                R.id.new_crime -> {
                    showNewCrime()
                    return true
                }
            }
            return false
        }
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

        // link on the activity like a host - for more shorter call
        val menuHost: MenuHost = requireActivity()

        // let the activity handle the menu in this fragment
        // -- viewLifecycleOwner controls the creation and destruction menu for this fragment.
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)

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

    private fun showNewCrime(){
        viewLifecycleOwner.lifecycleScope.launch{
            // default empty new crime
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false
            )

            // add this empty crime to database
            crimeListViewModel.addCrime(newCrime)

            // move to fragment when we can edit crime
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(newCrime.id)
            )
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