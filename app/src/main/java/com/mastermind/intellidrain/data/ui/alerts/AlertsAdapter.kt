package com.mastermind.intellidrain.ui.alerts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mastermind.intellidrain.data.model.Alert
import com.mastermind.intellidrain.databinding.ItemAlertBinding

class AlertsAdapter(
    private val items: List<Alert>,
    private val onClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alert = items[position]
        holder.binding.tvTitle.text = alert.title
        holder.binding.tvLocation.text = alert.location
        holder.binding.tvTime.text = alert.time
        holder.binding.tvAction.text = alert.recommendedAction

        val color = if (alert.priority == "CRITICAL") Color.parseColor("#FFCDD2") else Color.parseColor("#FFE0B2")
        holder.binding.cardRoot.setCardBackgroundColor(color)

        holder.binding.root.setOnClickListener { onClick(alert) }
    }

    override fun getItemCount() = items.size
}