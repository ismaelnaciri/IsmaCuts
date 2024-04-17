package cat.insvidreres.inf.ismacuts.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminDataBinding


class AdminDataFragment : Fragment() {

    private val sharedViewModel: AdminsSharedViewModel by activityViewModels()
    private lateinit var binding: FragmentAdminDataBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminDataBinding.inflate(inflater)

        binding.test2.text = sharedViewModel.adminEmail

        return binding.root
    }
}