package com.example.konser_ticket.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konser_ticket.R
import com.example.konser_ticket.databinding.FragmentUserHomeBinding
import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiClient
import com.example.konser_ticket.network.ApiService
import com.example.konser_ticket.prefManager.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var client: ApiService
    private lateinit var  prefManager: PrefManager
    private lateinit var konserList: ArrayList<Konser>
    private lateinit var adapterKonser: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        konserList = ArrayList()
        prefManager = PrefManager.getInstance(requireContext())
        val client = ApiClient.getInstance()
        with(binding) {
            adapterKonser = UserAdapter(konserList, client) { data ->
                val intent = Intent(requireContext(), UserDetailActivity::class.java).apply {
                    putExtra("id", data.id)
                    putExtra("title", data.title)
                    putExtra("artis", data.artis)
                    putExtra("location", data.location)
                    putExtra("date", data.date)
                    putExtra("price", data.price)
                    putExtra("description", data.description)
                }
                startActivity(intent)
            }
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.adapter = adapterKonser
            progressBar.visibility = View.VISIBLE
            titleNama.text = prefManager.getUsername()
            val email = prefManager.getEmail()
            Log.d("pref manager","email: $email")
            Log.d("Pref", "Username saved: ${prefManager.getUsername()}")

            // Data banner
            val bannerImages = listOf(R.drawable.img_3, R.drawable.img_4, R.drawable.img_5)
            val bannerAdapter = BannerAdapter(bannerImages)

            // Setup RecyclerView with LinearLayoutManager
            bannerRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = bannerAdapter

                // Set the RecyclerView to start near the middle for a seamless infinite loop
                val middlePosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerImages.size)
                scrollToPosition(middlePosition)

                // Optional: Smooth auto-scroll behavior for carousel effect
                postDelayed({ smoothScrollToPosition(middlePosition + 1) }, 3000)
            }
        }
        fetchKonserData()

        return binding.root
    }

    private fun fetchKonserData() {
        val client = ApiClient.getInstance()
        val response = client.getAllKonser()
        response.enqueue(object : Callback<List<Konser>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Konser>>, response: Response<List<Konser>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        konserList.clear()
                        konserList.addAll(data)
                        adapterKonser.notifyDataSetChanged()
                    } else {
                        Log.e("UserHomeFragment", "Data kosong")
                    }
                } else {
                    Log.e("UserHomeFragment", "Response gagal dengan kode: ${response.code()}")
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Konser>>, t: Throwable) {
                Log.e("UserHomeFragment", "Error: ${t.message}")
                binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}