package com.mastermind.intellidrain.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermind.intellidrain.databinding.FragmentDrainHistoryBinding

class DrainHistoryFragment : Fragment() {

    private var _binding: FragmentDrainHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val args: DrainHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrainHistoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDrainName.text = args.drainName

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility =
                if (loading) View.VISIBLE else View.GONE
        }

        viewModel.history.observe(viewLifecycleOwner) { records ->
            binding.recyclerView.adapter = HistoryAdapter(records)
        }

        viewModel.loadHistory(args.drainId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}