package com.neu.mobileapplicationdevelopment202430

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neu.mobileapplicationdevelopment202430.converter.HomeFragment
import com.neu.mobileapplicationdevelopment202430.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up view binding for the activity layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the HomeFragment on app launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, HomeFragment())
                .commit()
        }
    }
}
