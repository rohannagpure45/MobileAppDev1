package com.neu.mobileapplicationdevelopment202430.converter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.data.ConversionRepositoryImpl
import com.neu.mobileapplicationdevelopment202430.databinding.FragmentMvpBinding
import com.neu.mobileapplicationdevelopment202430.domain.ConvertUnitsUseCase
import com.neu.mobileapplicationdevelopment202430.domain.UnitType

class MVPFragment : Fragment(), UnitConverterView {

    private var _binding: FragmentMvpBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: UnitConverterPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMvpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the Presenter with the conversion use-case
        val conversionRepo = ConversionRepositoryImpl()
        val convertUseCase = ConvertUnitsUseCase(conversionRepo)
        presenter = UnitConverterPresenterImpl(convertUseCase)
        presenter.attach(this)

        // Set up unit selection spinners
        val units = UnitType.values()
        val unitNames = units.map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitNames).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter
        // Default selection example: from Kg to Pound
        binding.spinnerFrom.setSelection(0)
        binding.spinnerTo.setSelection(1)

        // Handle convert button click
        binding.buttonConvert.setOnClickListener {
            val fromUnit = units[binding.spinnerFrom.selectedItemPosition]
            val toUnit = units[binding.spinnerTo.selectedItemPosition]
            val inputValue = binding.inputValue.text.toString()
            presenter.onConvertClicked(inputValue, fromUnit, toUnit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()  // Avoid memory leaks by detaching view
        _binding = null
    }

    // Implementation of UnitConverterView interface:
    override fun showResult(result: String) {
        // Display the converted result in the TextView
        binding.textResult.text = result
        // Ensure the result text is in normal color (black)
        binding.textResult.setTextColor(Color.BLACK)
    }

    override fun showError(message: String) {
        // Show error message as a Toast for MVP
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
