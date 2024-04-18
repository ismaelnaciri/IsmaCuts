package cat.insvidreres.inf.ismacuts.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminDataBinding
import java.util.Calendar


class AdminDataFragment : Fragment() {

    private val sharedViewModel: AdminsSharedViewModel by activityViewModels()
    private lateinit var binding: FragmentAdminDataBinding
    private val viewModel: AdminDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminDataBinding.inflate(inflater)
        var currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        val toolbar = binding.profileBookingFragmentToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        val recyclerView = binding.adminDataQuoteRV
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.loadProfessional(sharedViewModel.adminEmail)
        viewModel.loadCurrentMonthRevenueData(sharedViewModel.adminEmail, currentMonth,
            onMonthOutOfBounds = {
                Toast.makeText(
                    requireContext(),
                    "No data found for currentMonth ${viewModel.loadMonth(currentMonth)}",
                    Toast.LENGTH_SHORT)
                    .show()
        },
            onComplete = {
                viewModel.loadTotalRevenue()
            })

        viewModel.professionalPropertiesList.observe(viewLifecycleOwner) { quoteList ->
            val quoteAdapter = AdminDataRecyclerAdapter(requireContext(), quoteList)
            recyclerView.adapter = quoteAdapter
        }

        return binding.root
    }
}