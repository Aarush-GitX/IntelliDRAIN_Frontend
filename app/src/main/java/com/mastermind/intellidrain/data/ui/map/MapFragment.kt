package com.mastermind.intellidrain.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mastermind.intellidrain.data.model.Drain
import com.mastermind.intellidrain.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().userAgentValue = requireContext().packageName
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(13.0)
        binding.mapView.controller.setCenter(GeoPoint(18.6298, 73.7997)) // Pimpri-Chinchwad default center

        viewModel.error.observe(viewLifecycleOwner) { message ->
            if (message != null) Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.drains.observe(viewLifecycleOwner) { drains ->
            addDrainMarkers(drains)
        }

        viewModel.loadDrains()
    }

    private fun addDrainMarkers(drains: List<Drain>) {
        binding.mapView.overlays.clear()

        drains.forEach { drain ->
            val marker = Marker(binding.mapView)
            marker.position = GeoPoint(drain.latitude, drain.longitude)
            marker.title = drain.area_name
            marker.snippet = "Risk: ${drain.risk_level} | Blockage: ${(drain.blockage_ratio * 100).toInt()}%"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            when (drain.risk_level) {
                "CRITICAL" -> marker.icon?.setTint(Color.RED)
                "ELEVATED" -> marker.icon?.setTint(Color.parseColor("#FFA000"))
                else -> marker.icon?.setTint(Color.parseColor("#388E3C"))
            }

            marker.setOnMarkerClickListener { clickedMarker, _ ->
                clickedMarker.showInfoWindow() // shows the title/snippet bubble first tap
                val action = MapFragmentDirections.actionMapFragmentToDrainDetailsFragment(drain.id)
                findNavController().navigate(action)
                true
            }

            binding.mapView.overlays.add(marker)
        }
        binding.mapView.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}