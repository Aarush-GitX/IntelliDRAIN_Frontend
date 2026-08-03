package com.mastermind.intellidrain.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mastermind.intellidrain.data.model.InspectionRecord
import com.mastermind.intellidrain.databinding.ItemHistoryBinding

class HistoryAdapter(private val items: List<InspectionRecord>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = items[position]
        holder.binding.tvDate.text = record.date
        holder.binding.tvBlockage.text = "Blockage: ${(record.blockage_ratio * 100).toInt()}%"
        holder.binding.tvStatus.text = record.status
        holder.binding.tvRiskLevel.text = record.risk_level
        holder.binding.tvRiskLevel.setBackgroundColor(
            when (record.risk_level) {
                "CRITICAL" -> Color.parseColor("#FFCDD2")
                "ELEVATED" -> Color.parseColor("#FFE0B2")
                else -> Color.parseColor("#C8E6C9")
            }
        )
    }

    override fun getItemCount() = items.size
}