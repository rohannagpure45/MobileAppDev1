package com.neu.mobileapplicationdevelopment202430.converter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        val conversionRepo = ConversionRepositoryImpl()
        val convertUseCase = ConvertUnitsUseCase(conversionRepo)
        presenter = UnitConverterPresenterImpl(convertUseCase)
        presenter.attach(this)

        val units = UnitType.values()
        // spinner adapters with placeholder at index 0
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
                Toast.makeText(requireContext(), "Please select both units", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            presenter.onConvertClicked(
                input,
                units[fromPos - 1],
                units[toPos - 1]
            )
        }
    }

    override fun showResult(result: String) {
        binding.textResult.text = "Result is $result"
        binding.textResult.setTextColor(Color.BLACK)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
        _binding = null
    }
}
