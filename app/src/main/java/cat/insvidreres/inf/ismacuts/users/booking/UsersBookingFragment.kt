package cat.insvidreres.inf.ismacuts.users.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersBookingBinding


class UsersBookingFragment : Fragment() {

    private lateinit var binding: FragmentUsersBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUsersBookingBinding.inflate(inflater)
        // Inflate the layout for this fragment


        return binding.root
    }

}