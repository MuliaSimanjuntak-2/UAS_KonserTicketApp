package com.example.konser_ticket.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.konser_ticket.databinding.ItemKonserBinding

import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiService
import java.text.SimpleDateFormat
import java.util.Locale

typealias onClickKonser = (Konser) -> Unit

class UserAdapter(
    private val listKonser: ArrayList<Konser>,
    private val client: ApiService,
    private val onClickKonser: onClickKonser
) : RecyclerView.Adapter<UserAdapter.ItemUserViewHolder>() {
    inner class ItemUserViewHolder(private val binding: ItemKonserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Konser) {
            with(binding) {
                titleEvent.text = data.title
                dateEvent.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(data.date))
                priceEvent.text = "Rp${data.price}"


                itemView.setOnClickListener {
                    onClickKonser(data)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        val binding = ItemKonserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemUserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listKonser.size
    }

    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        holder.bind(listKonser[position])
    }


}