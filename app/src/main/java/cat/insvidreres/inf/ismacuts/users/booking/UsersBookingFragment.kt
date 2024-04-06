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

        //TODO professionalsRV layout and check it works, click appointment and insert it
        binding = FragmentUsersBookingBinding.inflate(inflater)
        val dayRecyclerView = binding.bookingDaysRecyclerView
        val hoursRecyclerView = binding.availableHoursRecyclerView
        val professionalsRecyclerView = binding.professionalsBookingRV

        dayRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hoursRecyclerView.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        professionalsRecyclerView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)

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

        val professionalAdapter = ProfessionalAdapter(requireContext(), emptyList()) { selectedProfessional ->
            Toast.makeText(
                requireContext(),
                "Professional Selected: " + selectedProfessional.name,
                Toast.LENGTH_LONG
            ).show()
        }

        viewModel.loadDays()
        viewModel.loadHours()
        viewModel.loadProfessionals()

        viewModel.days.observe(viewLifecycleOwner) { daysList ->
            dayAdapter.dataset = daysList
            binding.bookingDaysRecyclerView.adapter = dayAdapter
        }

        viewModel.hours.observe(viewLifecycleOwner) { hoursList ->
            hourAdapter.dataset = hoursList
            binding.availableHoursRecyclerView.adapter = hourAdapter
        }

        viewModel.professionals.observe(viewLifecycleOwner) { professionalList ->
            professionalAdapter.dataset = professionalList
            binding.professionalsBookingRV.adapter = professionalAdapter
        }

        return binding.root
    }

}