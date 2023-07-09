package com.example.criminalintent.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.criminalintent.R
import com.example.criminalintent.fragments.CrimeListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //if fragment exist in the list of fragment, than do nothing, otherwise create and add fragment
/*        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            val fragment = CrimeListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }*/
    }
}