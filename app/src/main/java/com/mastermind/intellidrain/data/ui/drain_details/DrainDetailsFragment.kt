package com.mastermind.intellidrain.ui.drain_details

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermind.intellidrain.databinding.FragmentDrainDetailsBinding

class DrainDetailsFragment : Fragment() {

    private var _binding: FragmentDrainDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrainDetailsViewModel by viewModels()
    private val args: DrainDetailsFragmentArgs by navArgs() // auto-generated from nav_graph's "drainId" argument

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrainDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            if (message != null) Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.drain.observe(viewLifecycleOwner) { drain ->
            binding.tvAreaName.text = drain.area_name
            binding.tvRiskLevel.text = drain.risk_level
            binding.tvRiskLevel.setBackgroundColor(
                when (drain.risk_level) {
                    "CRITICAL" -> Color.parseColor("#FFCDD2")
                    "ELEVATED" -> Color.parseColor("#FFE0B2")
                    else -> Color.parseColor("#C8E6C9")
                }
            )
            binding.tvBlockage.text = "Blockage: ${(drain.blockage_ratio * 100).toInt()}%"
            binding.tvRainfall.text = "Rainfall (last hour): ${drain.rainfall_last_hour_mm} mm"
            binding.tvHumidity.text = "Humidity: ${drain.humidity_pct}%"
            binding.tvTemperature.text = "Temperature: ${drain.temperature_c}°C"
            binding.tvLastInspection.text = "Last Inspection: ${drain.last_inspection_date}"
            binding.tvLastCleaning.text = "Last Cleaning: ${drain.last_cleaning_date ?: "N/A"}"
            binding.tvRecommendation.text = drain.dispatch_action

            binding.btnOpenNavigation.setOnClickListener {
                openGoogleMapsNavigation(drain.latitude, drain.longitude)
            }
        }

        binding.btnViewHistory.setOnClickListener {
            Toast.makeText(requireContext(), "History screen coming next", Toast.LENGTH_SHORT).show()
            // we'll wire this to the real History screen in the next step
        }

        viewModel.loadDrain(args.drainId)
        binding.btnViewHistory.setOnClickListener {
            val action = DrainDetailsFragmentDirections
                .actionDrainDetailsFragmentToHistoryFragment(args.drainId)
            findNavController().navigate(action)
        }
    }

    private fun openGoogleMapsNavigation(lat: Double, lon: Double) {
        val uri = Uri.parse("google.navigation:q=$lat,$lon")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}