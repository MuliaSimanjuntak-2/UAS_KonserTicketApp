package com.example.konser_ticket.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konser_ticket.R
import com.example.konser_ticket.database.KonserDao
import com.example.konser_ticket.database.KonserDatabase
import com.example.konser_ticket.databinding.FragmentUserBookingBinding
import java.util.concurrent.ExecutorService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserBookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentUserBookingBinding
    private lateinit var adapter: BookingAdapter
    private lateinit var konserDao: KonserDao
    private lateinit var executorService: ExecutorService


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
        // Inflate the layout for this fragment
        binding = FragmentUserBookingBinding.inflate(inflater, container, false)
        val db = KonserDatabase.getDatabase(requireContext())
        konserDao = db!!.KonserDao()
        GetRecyclerView()
        return binding.root
    }

    private fun GetRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookingAdapter(requireContext()) { data ->
            val intent = Intent(requireContext(), UserDetailBookingActivity::class.java).apply {
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
        binding.recyclerView.adapter = adapter
        konserDao.getAllKonser().observe(viewLifecycleOwner) { datalist ->
            adapter.submitList(datalist)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserBookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserBookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}