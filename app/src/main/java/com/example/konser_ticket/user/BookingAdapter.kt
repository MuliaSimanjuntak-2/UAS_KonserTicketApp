package com.example.konser_ticket.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konser_ticket.database.KonserRoom
import com.example.konser_ticket.databinding.ItemBuyBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BookingAdapter(
    private val context: Context,
    private val Detail: (KonserRoom) -> Unit
) : ListAdapter<KonserRoom, BookingAdapter.ItemBuyViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<KonserRoom>() {
        override fun areItemsTheSame(oldItem: KonserRoom, newItem: KonserRoom): Boolean {
            return oldItem.id == newItem.id // Check if the IDs are the same
        }

        override fun areContentsTheSame(oldItem: KonserRoom, newItem: KonserRoom): Boolean {
            return oldItem == newItem // Check if the content is the same
        }
    }

    inner class ItemBuyViewHolder(private val binding: ItemBuyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: KonserRoom) {
            with(binding) {
                titleEvent.text = data.title
                dateEvent.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(data.date))
                priceEvent.text = "Rp${data.price}"

                // Handle delete button click
                itemView.setOnClickListener{
                    Detail(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBuyViewHolder {
        val binding = ItemBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBuyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemBuyViewHolder, position: Int) {
        holder.bind(getItem(position)) // Only pass the data to bind
    }

}