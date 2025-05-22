package com.neu.mobileapplicationdevelopment202430.converter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.neu.mobileapplicationdevelopment202430.data.ConversionRepositoryImpl
import com.neu.mobileapplicationdevelopment202430.databinding.FragmentMvvmBinding
import com.neu.mobileapplicationdevelopment202430.domain.ConvertUnitsUseCase
import com.neu.mobileapplicationdevelopment202430.domain.UnitType

class MVVMFragment : Fragment() {

    private var _binding: FragmentMvvmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConverterViewModel by activityViewModels {
        ConverterViewModel.Factory(ConvertUnitsUseCase(ConversionRepositoryImpl()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMvvmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val units = UnitType.values()
        val fromList = listOf("From Unit") + units.map { it.displayName }
        val toList   = listOf("To Unit")   + units.map { it.displayName }

        val fromAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            fromList
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val toAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            toList
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerFrom.adapter = fromAdapter
        binding.spinnerTo.adapter   = toAdapter

        binding.spinnerFrom.setSelection(0)
        binding.spinnerTo.setSelection(0)

        binding.buttonConvert.setOnClickListener {
            val fromPos = binding.spinnerFrom.selectedItemPosition
            val toPos   = binding.spinnerTo.selectedItemPosition
            val input   = binding.inputValue.text.toString().trim()

            if (fromPos == 0 || toPos == 0) {
                binding.textResult.text = "Please select both units"
                binding.textResult.setTextColor(Color.RED)
                return@setOnClickListener
            }
            viewModel.convert(input, units[fromPos - 1], units[toPos - 1])
        }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
