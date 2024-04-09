package cat.insvidreres.inf.ismacuts.users.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersBookingBinding
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel
import cat.insvidreres.inf.ismacuts.users.home.Product


class UsersBookingFragment : Fragment() {

    private lateinit var binding: FragmentUsersBookingBinding
    private val viewModel: UserBookingViewModel by viewModels()
    private val bookingSharedViewModel: HomeBookingSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO professionalsRV layout and check it works, click appointment and insert it
        binding = FragmentUsersBookingBinding.inflate(inflater)
        val dayRecyclerView = binding.bookingDaysRecyclerView
        val hoursRecyclerView = binding.availableHoursRecyclerView
        val professionalsRecyclerView = binding.professionalsBookingRV

        val toolbar = binding.bookingFragmentToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        dayRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hoursRecyclerView.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        professionalsRecyclerView.layoutManager =
            LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)

        val dayAdapter = DaysAdapter(requireContext(), emptyList()) { selectedDay ->

            viewModel.updateSelectedItems(selectedDay,
                onError = {
                    Toast.makeText(
                        requireContext(),
                        "selectedOptions size: ${viewModel.selectedOptions.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDelete = {
                    Toast.makeText(
                        requireContext(),
                        "Deleted item: $selectedDay",
                        Toast.LENGTH_SHORT
                    ).show()
                })


            bookingSharedViewModel.updateSelectedItems(selectedDay,
                onError = {
                    print("day fuck gg item")
                },
                onDelete = {
                    print("${selectedDay} deleted from updateSelectedItems")
                })
        }

        val hourAdapter = HourAdapter(requireContext(), emptyList()) { selectedHour ->
            Toast.makeText(
                requireContext(),
                "Hour selected " + selectedHour.hour,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.updateSelectedItems(selectedHour,
                onError = {
                    Toast.makeText(
                        requireContext(),
                        "Error wtf",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDelete = {
                    Toast.makeText(
                        requireContext(),
                        "Deleted item: $selectedHour",
                        Toast.LENGTH_SHORT
                    ).show()
                })

            bookingSharedViewModel.updateSelectedItems(selectedHour,
                onError = {
                    print("hour fuck gg item")
                },
                onDelete = {
                    print("${selectedHour.hour} deleted from updateSelectedItems")
                })
        }

        val professionalAdapter =
            ProfessionalAdapter(requireContext(), emptyList()) { selectedProfessional ->
                Toast.makeText(
                    requireContext(),
                    "Professional Selected: " + selectedProfessional.name,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.updateSelectedItems(selectedProfessional.email,
                    onError = {
                        Toast.makeText(
                            requireContext(),
                            "Error wtf",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDelete = {
                        Toast.makeText(
                            requireContext(),
                            "Deleted item: $selectedProfessional",
                            Toast.LENGTH_SHORT
                        ).show()
                    })

                bookingSharedViewModel.updateSelectedItems(selectedProfessional.email,
                    onError = {
                        print("professional fuck gg item")
                    },
                    onDelete = {
                        print("${selectedProfessional.email} deleted from updateSelectedItems")
                    })

                if (bookingSharedViewModel.selectedOptions.any { it.toString() == selectedProfessional.email }) {
                    viewModel.loadHours(selectedProfessional.name)
                } else {
                    viewModel.resetHoursArray()
                }
                hourAdapter.notifyDataSetChanged()
            }

        viewModel.loadDays()
        for (item in bookingSharedViewModel.selectedOptions) {
            if (item is Product) {
                viewModel.loadProfessionals(item.serviceType)
            }
        }

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

        binding.confirmBookingButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "selected items: ${viewModel.selectedOptions}",
                Toast.LENGTH_LONG
            ).show()

            println("selected items: ${viewModel.selectedOptions}")

            var name = ""
            var hour = ""

            for (item in viewModel.selectedOptions) {

                when (item) {
                    is Professional -> {
                        name = item.name
                    }

                    is Hour -> {
                        hour = item.hour
                    }

                }
            }

            if (name != "" && hour != "" && viewModel.selectedOptions.size == 3) {
                viewModel.removeHourFromProfessional(name, hour)
                viewModel.selectedOptions.clear()
                viewModel.resetHoursArray()

                if (bookingSharedViewModel.checkIfAnyItemNull()) {
                    bookingSharedViewModel.generateBookingInsert()
                    bookingSharedViewModel.insertBooking()
                }

                hourAdapter.notifyDataSetChanged()
            } else {
                println("the name or hour in the selectedOptions were empty, ${viewModel.selectedOptions}")
            }
        }

        return binding.root
    }

}