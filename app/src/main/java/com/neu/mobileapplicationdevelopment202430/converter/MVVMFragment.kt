package com.neu.mobileapplicationdevelopment202430.converter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.databinding.FragmentMvvmBinding
import com.neu.mobileapplicationdevelopment202430.domain.UnitType

class MVVMFragment : Fragment() {

    private var _binding: FragmentMvvmBinding? = null
    private val binding get() = _binding!!
    // Obtain ViewModel (scoped to Activity to survive configuration changes)
    private val viewModel: ConverterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMvvmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up unit spinners (same units list as in MVPFragment)
        val units = UnitType.values()
        val unitNames = units.map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitNames).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter
        // Default selection (Kg to Pound)
        binding.spinnerFrom.setSelection(0)
        binding.spinnerTo.setSelection(1)

        // Observe ViewModel LiveData for results and errors
        viewModel.conversionResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ConversionUiState.Success -> {
                    binding.textResult.text = "Result is ${state.resultText}"
                    binding.textResult.setTextColor(Color.BLACK)
                }
                is ConversionUiState.Error -> {
                    binding.textResult.text = state.errorText
                    binding.textResult.setTextColor(Color.RED)
                }
            }
        }

        // Handle convert button click: trigger ViewModel conversion
        binding.buttonConvert.setOnClickListener {
            val fromUnit = units[binding.spinnerFrom.selectedItemPosition]
            val toUnit = units[binding.spinnerTo.selectedItemPosition]
            val inputValue = binding.inputValue.text.toString()
            viewModel.convert(inputValue, fromUnit, toUnit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
