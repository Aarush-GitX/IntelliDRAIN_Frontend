package com.mastermind.intellidrain.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mastermind.intellidrain.R
import com.mastermind.intellidrain.data.model.Drain
import com.mastermind.intellidrain.databinding.FragmentHomeBinding
import com.mastermind.intellidrain.ui.map.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.util.Calendar
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().userAgentValue = requireContext().packageName
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Greeting
        setupGreeting()

        // Dashboard
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility =
                if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            binding.tvError.visibility =
                if (message != null) View.VISIBLE else View.GONE

            binding.tvError.text = message
        }

        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalDrains.text = stats.totalDrains.toString()
            binding.tvCriticalDrains.text = stats.criticalDrains.toString()

            binding.tvAttention.text =
                if (stats.criticalDrains > 0) {
                    "${stats.criticalDrains} drains currently need attention."
                } else {
                    "All drains are currently stable."
                }
        }

        // Map
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)

        binding.mapView.controller.setZoom(13.0)
        binding.mapView.controller.setCenter(
            GeoPoint(18.6298, 73.7997)
        )

        mapViewModel.error.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mapViewModel.drains.observe(viewLifecycleOwner) { drains ->
            addDrainMarkers(drains)
        }

        mapViewModel.loadDrains()

        // Load dashboard
        viewModel.loadDashboard()

        // Toolbar
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.action_profile -> {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_settingsFragment
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }

        binding.tvGreeting.text = greeting
    }

    private fun addDrainMarkers(drains: List<Drain>) {
        binding.mapView.overlays.clear()

        drains.forEach { drain ->

            val marker = Marker(binding.mapView)

            marker.position = GeoPoint(
                drain.latitude,
                drain.longitude
            )

            marker.title = drain.area_name

            marker.snippet =
                "Risk: ${drain.risk_level} | Blockage: ${(drain.blockage_ratio * 100).toInt()}%"

            marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )


            marker.icon = ContextCompat.getDrawable(
                requireContext(),
                org.osmdroid.library.R.drawable.marker_default
            )?.mutate()

            val markerColor = when (drain.risk_level.trim().uppercase()) {
                "CRITICAL" -> Color.RED
                "ELEVATED" -> Color.parseColor("#FFA000")
                else -> Color.parseColor("#388E3C")
            }

            marker.icon?.setTint(markerColor)

            marker.setOnMarkerClickListener { clickedMarker, _ ->

                clickedMarker.showInfoWindow()

                val action =
                    HomeFragmentDirections
                        .actionHomeFragmentToDrainDetailsFragment(
                            drain.id
                        )

                findNavController().navigate(action)

                true
            }

            binding.mapView.overlays.add(marker)
        }

        binding.mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
    }

    override fun onPause() {
        _binding?.mapView?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        _binding?.mapView?.onDetach()
        super.onDestroyView()
        _binding = null
    }
}
