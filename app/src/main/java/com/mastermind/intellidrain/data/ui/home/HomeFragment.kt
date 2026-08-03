package com.mastermind.intellidrain.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mastermind.intellidrain.R
import com.mastermind.intellidrain.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            binding.tvError.visibility = if (message != null) View.VISIBLE else View.GONE
            binding.tvError.text = message
        }

        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalDrains.text = stats.totalDrains.toString()
            binding.tvCriticalDrains.text = stats.criticalDrains.toString()
            binding.tvHighRiskAreas.text = stats.highRiskAreas.toString()
            binding.tvPendingInspections.text = stats.pendingInspections.toString()
        }

        binding.btnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.mapFragment)
        }

        viewModel.loadDashboard()

        binding.toolbar.inflateMenu(R.menu.home_toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_profile) {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            } else false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}