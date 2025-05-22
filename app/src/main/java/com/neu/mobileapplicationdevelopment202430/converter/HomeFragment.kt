package com.neu.mobileapplicationdevelopment202430.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using ViewBinding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Navigate to MVP Converter fragment
        binding.buttonMvp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MVPFragment())
                .addToBackStack(null)
                .commit()
        }
        // Navigate to MVVM Converter fragment
        binding.buttonMvvm.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MVVMFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // avoid memory leaks
    }
}
