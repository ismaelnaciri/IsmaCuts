package cat.insvidreres.inf.ismacuts.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsMainActivity
import cat.insvidreres.inf.ismacuts.databinding.FragmentProfileBinding
import cat.insvidreres.inf.ismacuts.databinding.FragmentSettingsBinding
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater)

        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        val isNightMode = sharedPreferences.getBoolean("nightMode", false)
        binding.settingsDarkModeSwitch.isChecked = isNightMode

        binding.settingsDarkModeSwitch.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (binding.settingsDarkModeSwitch.isChecked) {
                editor.remove("nightMode")
                editor.putBoolean("nightMode", true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                editor.remove("nightMode")
                editor.putBoolean("nightMode", false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            editor.apply()
        }

        return binding.root
    }

    private fun saveSettings() {
        val editor = sharedPreferences.edit()
        editor.apply()
    }
}