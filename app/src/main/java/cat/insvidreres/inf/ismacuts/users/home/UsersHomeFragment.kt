package cat.insvidreres.inf.ismacuts.users.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersHomeBinding


class UsersHomeFragment : Fragment() {

    private lateinit var binding: FragmentUsersHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUsersHomeBinding.inflate(inflater)
        // Inflate the layout for this fragment


        return binding.root
    }


}