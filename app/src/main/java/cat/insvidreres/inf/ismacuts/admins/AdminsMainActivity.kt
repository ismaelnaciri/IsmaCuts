package cat.insvidreres.inf.ismacuts.admins

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ActivityAdminsMainBinding

class AdminsMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminsMainBinding
    private lateinit var navController: NavController

    private val adminsSharedViewModel: AdminsSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminEmail = intent.getStringExtra("adminEmail")

        if (!adminEmail.isNullOrEmpty()) {
            adminsSharedViewModel.adminEmail = adminEmail
        }

        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerViewTag.id) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomMenuView
        setupWithNavController(bottomNavigationView, navController)
    }
}