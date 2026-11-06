package com.mastermind.intellidrain.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mastermind.intellidrain.databinding.ItemDrainSelectionBinding

class DrainSelectionAdapter(
    private val drains: List<DrainItem>,
    private val onDrainClick: (DrainItem) -> Unit
) : RecyclerView.Adapter<DrainSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemDrainSelectionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDrainSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val drain = drains[position]

        holder.binding.tvDrainName.text = drain.name
        holder.binding.tvDrainLocation.text = drain.location

        holder.binding.root.setOnClickListener {
            onDrainClick(drain)
        }
    }

    override fun getItemCount(): Int = drains.size
}