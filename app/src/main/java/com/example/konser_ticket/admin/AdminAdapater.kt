package com.example.konser_ticket.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.konser_ticket.databinding.ItemAdminBinding
import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


typealias onClickKonser = (Konser) -> Unit

class AdminAdapater(
    private val listKonser: ArrayList<Konser>,
    private val client : ApiService,
    private val onClickKonser: onClickKonser,
    ) : RecyclerView.Adapter<AdminAdapater.ItemAdminViewHolder>() {
    inner class ItemAdminViewHolder(private val binding : ItemAdminBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Konser){
            with(binding){
                titleEvent.text = data.title
                dateEvent.text = data.date
                priceEvent.text = data.price


                itemView.setOnClickListener{
                    onClickKonser(data)
                }

                btnEdit.setOnClickListener{
                    val intentEdit = Intent(itemView.context, AdminUpdateActivity::class.java).apply {
                        putExtra("id", data.id)
                        putExtra("title", data.title)
                        putExtra("artis", data.artis)
                        putExtra("location", data.location)
                        putExtra("date", data.date)
                        putExtra("price", data.price)
                        putExtra("description", data.description)
                    }
                    itemView.context.startActivity(intentEdit)

                }
                btnDelete.setOnClickListener {
                    val response = data.id?.let { it1 -> client.deleteKonser(it1) }
                    response?.enqueue(object : Callback<Konser> {
                        override fun onResponse(
                            call: Call<Konser>,
                            response: Response<Konser>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    itemView.context,
                                    "data konser ${data.title} berhasil dihapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    removeItem(position)
                                }
                            } else {
                                Log.e("API Error", "Response not successful or body is null")
                            }
                        }

                        override fun onFailure(call: Call<Konser>, t: Throwable) {
                            t.message?.let { it1 -> Log.e("Error", it1) }
                        }
                    })
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdminViewHolder {
        val binding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAdminViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listKonser.size
    }

    override fun onBindViewHolder(holder: ItemAdminViewHolder, position: Int) {
        holder.bind(listKonser[position])
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Konser>) {
        listKonser.clear()
        listKonser.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        listKonser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, listKonser.size)
    }
}