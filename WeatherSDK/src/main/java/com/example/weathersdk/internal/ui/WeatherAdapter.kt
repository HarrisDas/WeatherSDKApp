package com.example.weathersdk.internal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathersdk.R
import com.example.weathersdk.databinding.ItemForecastBinding
import com.example.weathersdk.internal.domain.model.Forecast

internal class WeatherAdapter : RecyclerView.Adapter<ForecastListViewViewHolder>() {

    private val items = mutableListOf<Forecast>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ForecastListViewViewHolder(
        binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(
        holder: ForecastListViewViewHolder,
        position: Int
    ) = holder.bind(items[position])

    @SuppressLint("NotifyDataSetChanged")
    fun submit(items: List<Forecast>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

internal class ForecastListViewViewHolder(private val binding: ItemForecastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Forecast) {
        binding.tvTime.text = item.timestamp
        binding.tvTemperature.text =
            binding.tvTemperature.context.getString(
                R.string.temperature_label,
                item.temperature.toString()
            )
        binding.tvWeatherStatus.text = item.description
    }
}