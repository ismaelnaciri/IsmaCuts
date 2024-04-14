package cat.insvidreres.inf.ismacuts.admins.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminsHomeBinding

class AdminsHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminsHomeBinding
    private val viewModel: AdminsHomeViewModel by viewModels()
    private val adminViewModel: AdminsSharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminsHomeBinding.inflate(inflater)
        val bookingRecyclerView = binding.bookingAdminRV

        bookingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        var adminBookingAdapter: AdminBookingAdapter? = null
        adminBookingAdapter = AdminBookingAdapter(
            requireContext(),
            mutableListOf(),
            confirmClickListener = { selectedBooking ->
                println("inside confirm listener")
                viewModel.confirmBooking(adminViewModel.adminEmail, selectedBooking) {
                    adminBookingAdapter?.dataset?.remove(selectedBooking)
                    adminBookingAdapter?.notifyDataSetChanged()
                }
            },
            deleteClickListener = { selectedBooking ->
                println("inside delete listener")

                viewModel.deleteBooking(adminViewModel.adminEmail, selectedBooking) {
                    adminBookingAdapter?.dataset?.remove(selectedBooking)
                    adminBookingAdapter?.notifyDataSetChanged()
                }
            },
        )

        println("adminEmail arrive at main activity?  ${adminViewModel.adminEmail}")
        viewModel.loadBookings(adminViewModel.adminEmail)

        viewModel.bookings.observe(viewLifecycleOwner) { booksList ->
            adminBookingAdapter.dataset = booksList
            binding.bookingAdminRV.adapter = adminBookingAdapter
        }

        return binding.root
    }
}