package cat.insvidreres.inf.ismacuts.admins.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminsHomeBinding

class AdminsHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminsHomeBinding
    private val viewModel: AdminsHomeViewModel by viewModels()
    private val adminViewModel: AdminsSharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminsHomeBinding.inflate(inflater)
        val bookingRecyclerView = binding.bookingAdminRV

        bookingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.loadBookings(adminViewModel.adminEmail)

        viewModel.bookings.observe(viewLifecycleOwner) { booksList ->
            val adapter = AdminBookingAdapter(requireContext(), booksList)
        }
        return binding.root
    }
}