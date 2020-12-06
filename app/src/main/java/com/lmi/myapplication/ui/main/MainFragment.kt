package com.lmi.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lmi.myapplication.databinding.ItemRecommendationBaseRowBindingImpl
import com.lmi.myapplication.databinding.ItemRecommendationRowBinding
import com.lmi.myapplication.databinding.MainFragmentBinding
import com.lmi.myapplication.ui.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var binding: MainFragmentBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        dayLenghtPicker.minValue = 1
        dayLenghtPicker.maxValue = 30
        dayLenghtPicker.value = 10

        binding.viewModel = viewModel
        populateRecommendationTable(viewModel)

        button.setOnClickListener {
            viewModel.makeRecommendation(
                stockSymbolEntry.text.toString(),
                dayLenghtPicker.value,
                socialMediaPicker.selectedItem.toString()
            )
        }
    }

    private fun populateRecommendationTable(viewModel: MainViewModel) {
        viewModel.recommendations.observe(this, { recommendations ->
            table.removeAllViews()

            recommendations.forEachIndexed { index, recommendationRowViewModel ->
                val binding = if (index == 0) {
                    val binding = ItemRecommendationBaseRowBindingImpl.inflate(layoutInflater)
                    binding.viewModel = recommendationRowViewModel
                    binding
                } else {
                    val binding = ItemRecommendationRowBinding.inflate(layoutInflater)
                    binding.viewModel = recommendationRowViewModel
                    binding
                }

                binding.lifecycleOwner = this
                table.addView(binding.root)
            }
        })
    }
}