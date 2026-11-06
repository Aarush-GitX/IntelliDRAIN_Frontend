
package com.mastermind.intellidrain.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermind.intellidrain.data.repository.DrainRepository
import com.mastermind.intellidrain.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val repository = DrainRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        loadDrains()
    }

    private fun loadDrains() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val drains = repository.getAllDrains()

                if (_binding == null) return@launch

                val drainItems = drains.map { drain ->
                    DrainItem(
                        id = drain.id,
                        name = "Drain %02d".format(drain.id),
                        location = drain.area_name
                    )
                }

                binding.recyclerView.adapter =
                    DrainSelectionAdapter(drainItems) { drain ->
                        val action =
                            HistoryFragmentDirections
                                .actionHistoryFragmentToDrainHistoryFragment(
                                    drainId = drain.id,
                                    drainName = drain.name
                                )

                        findNavController().navigate(action)
                    }

                if (drainItems.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "No drains found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                if (_binding == null) return@launch

                Toast.makeText(
                    requireContext(),
                    "Failed to load drains. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class DrainItem(
    val id: Int,
    val name: String,
    val location: String
)
