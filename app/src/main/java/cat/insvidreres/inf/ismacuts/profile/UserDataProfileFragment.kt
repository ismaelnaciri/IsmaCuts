package cat.insvidreres.inf.ismacuts.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.UserSharedViewModel
import cat.insvidreres.inf.ismacuts.admins.AdminsMainActivity
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.admins.home.AdminBooking
import cat.insvidreres.inf.ismacuts.databinding.FragmentUserDataProfileBinding
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity

class UserDataProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserDataProfileBinding
    private val sharedViewModel: HomeBookingSharedViewModel by activityViewModels()
    private val viewModel: UserDataProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserDataProfileBinding.inflate(inflater)

        //Handle top back arrow
        val toolbar = binding.profileBookingFragmentToolbar
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        //Handle hardware back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        val recyclerView = binding.profileUsersBookingsRV
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.loadBookings(sharedViewModel.userEmail)
        viewModel.bookings.observe(viewLifecycleOwner) { bookingsList ->
            val bookAdapter = ProfileMyBookingAdapter(requireContext(), bookingsList)
            recyclerView.adapter = bookAdapter
        }

        return binding.root
    }

}