package cat.insvidreres.inf.ismacuts.admins.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminsHomeBinding
import java.util.Calendar

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
        var currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        var currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        bookingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.loadProfessionalName(adminViewModel.adminEmail)
        viewModel.professionalName.observe(viewLifecycleOwner) {
            binding.adminHomeGreetingTV.text = "Greetings, $it"
        }

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
        viewModel.loadCurrentMonthRevenueData(adminViewModel.adminEmail, currentMonth,
            onMonthOutOfBounds = {
                Toast.makeText(
                    requireContext(),
                    "No data found for currentMonth ${viewModel.loadMonth(currentMonth)}",
                    Toast.LENGTH_SHORT)
                    .show()
            },
            onComplete = {
                viewModel.loadTodayRevenue(currentDay)
            })

        viewModel.bookings.observe(viewLifecycleOwner) { booksList ->
            adminBookingAdapter.dataset = booksList
            binding.bookingAdminRV.adapter = adminBookingAdapter
        }

        viewModel.todayRevenue.observe(viewLifecycleOwner) { value ->
            binding.adminHomeTodayTotalRevenueTV.text = "Today's Revenue: $value €"
        }

        return binding.root
    }
}