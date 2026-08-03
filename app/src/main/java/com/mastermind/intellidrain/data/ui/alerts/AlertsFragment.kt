package com.mastermind.intellidrain.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermind.intellidrain.databinding.FragmentAlertsBinding

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlertsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            binding.recyclerView.adapter = AlertsAdapter(alerts) { alert ->
                val action = AlertsFragmentDirections
                    .actionAlertsFragmentToDrainDetailsFragment(alert.drainId)
                findNavController().navigate(action)
            }
        }

        viewModel.loadAlerts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}