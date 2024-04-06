package cat.insvidreres.inf.ismacuts.users.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersBookingBinding


class UsersBookingFragment : Fragment() {

    private lateinit var binding: FragmentUsersBookingBinding
    private val viewModel: UserBookingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUsersBookingBinding.inflate(inflater)
        val dayRecyclerView = binding.bookingDaysRecyclerView
        val hoursRecyclerView = binding.availableHoursRecyclerView

        dayRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hoursRecyclerView.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        val dayAdapter = DaysAdapter(requireContext(), emptyList()) {selectedDay ->
            Toast.makeText(
                requireContext(),
                "Day selected " + selectedDay.day + " " + selectedDay.dayOfWeek,
                Toast.LENGTH_LONG
            ).show()
        }

        val hourAdapter = HourAdapter(requireContext(), emptyList()) { selectedHour ->
            Toast.makeText(
                requireContext(),
                "Hour selected " + selectedHour.hour,
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.loadDays()
        viewModel.loadHours()

        viewModel.days.observe(viewLifecycleOwner) { daysList ->
            dayAdapter.dataset = daysList
            binding.bookingDaysRecyclerView.adapter = dayAdapter
        }

        viewModel.hours.observe(viewLifecycleOwner) { hoursList ->
            hourAdapter.dataset = hoursList
            binding.availableHoursRecyclerView.adapter = hourAdapter
        }

        return binding.root
    }

}