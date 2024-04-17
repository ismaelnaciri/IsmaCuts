package cat.insvidreres.inf.ismacuts.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsMainActivity
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentProfileBinding
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private lateinit var sharedViewModel: Any
    private lateinit var binding: FragmentProfileBinding
    private val viewModel : ProfileViewModel by viewModels()
    private var imageUri: Uri? = null
    private var currentImgUrl: Any? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        sharedViewModel = if (activity is UsersMainActivity) {
            activityViewModels<HomeBookingSharedViewModel>().value
        } else if (activity is AdminsMainActivity) {
            activityViewModels<AdminsSharedViewModel>().value
        } else {
            throw IllegalStateException("Unknown activity type")
        }

        val email = when (sharedViewModel) {
            is HomeBookingSharedViewModel -> {
                (sharedViewModel as HomeBookingSharedViewModel).userEmail
            }
            is AdminsSharedViewModel -> {
                (sharedViewModel as AdminsSharedViewModel).adminEmail
            }
            else -> throw IllegalStateException("Unknown sharedViewModel type")
        }

        println("EMAIL RECEIVED | $email")

        if (activity is UsersMainActivity) {
            (sharedViewModel as HomeBookingSharedViewModel).userEmail = email
            binding.profileGoToBookingsFragment.text = "MY BOOKINGS"
        } else if (activity is AdminsMainActivity) {
            (sharedViewModel as AdminsSharedViewModel).adminEmail = email
            binding.profileGoToBookingsFragment.text = "YOUR STATS"
        }

        binding.profileGoToBookingsFragment.setOnClickListener {
            if (activity is UsersMainActivity) {
                if (currentImgUrl.toString() == imageUri.toString()) {
                    viewModel.updateProfilePic(email, false, imageUri)
                }

                findNavController().navigate(R.id.userDataProfileFragment)
            } else if (activity is AdminsMainActivity) {
                if (currentImgUrl.toString() == imageUri.toString()) {
                    viewModel.updateProfilePic(email, true, imageUri)
                }
                findNavController().navigate(R.id.adminDataFragment)
            }
        }

        if (activity is AdminsMainActivity) {
            viewModel.loadProfessional(email)
            println("professional? ${viewModel.professional.value}")
            viewModel.professional.observe(viewLifecycleOwner) { professional ->
                binding.profileNameTV.text = professional.name
                binding.profileEmailTV.text = professional.email
                binding.profileUserNameTV.text = professional.name
                binding.profileUserPassword.text = professional.password
                binding.profileIsAdminTV.text = professional.services.toString().substring(1, professional.services.toString().length - 1)

                binding.profilePictureIV.setOnClickListener {
                    resultLauncher.launch("image/*")
                }

                Glide.with(binding.profilePictureIV.context).load(professional.img).into(binding.profilePictureIV)
                currentImgUrl = professional.img
            }

        } else if (activity is UsersMainActivity) {
            viewModel.loadUser(email)
            println("user? ${viewModel.user.value}")
            viewModel.user.observe(viewLifecycleOwner) { user ->
                binding.profileNameTV.text = user.username
                binding.profileEmailTV.text = user.email
                binding.profileUserNameTV.text = user.username

                val sp = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                val password = sp.getString("password", "")

                if (!password.isNullOrEmpty()) {
                    binding.profileUserPassword.text = password
                } else {
                    binding.profileUserPassword.text = "Click on the remember password to see it here!"
                }

                binding.profileIsAdminTV.text = user.admin.toString()

                binding.profilePictureIV.setOnClickListener {
                    resultLauncher.launch("image/*")
                }
                Glide.with(binding.profilePictureIV.context).load(user.img).into(binding.profilePictureIV)

            }
        }

        return binding.root
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){

        imageUri = it
        binding.profilePictureIV.setImageURI(it)
    }

}