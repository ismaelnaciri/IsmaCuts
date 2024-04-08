package cat.insvidreres.inf.ismacuts.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cat.insvidreres.inf.ismacuts.databinding.ActivityUsersMainBinding

class UsersMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersMainBinding
    private lateinit var navController: NavController

    private val bookingSharedViewModel: HomeBookingSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("userEmail")

        if (userEmail != null) {
            bookingSharedViewModel.updateSelectedItems(userEmail,
                onError = {
                    print("professional fuck gg item")
                },
                onDelete = {
                    print("${userEmail} deleted from updateSelectedItems")
                })
        }
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerViewTag.id) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomMenuView
        setupWithNavController(bottomNavigationView, navController)
    }
}