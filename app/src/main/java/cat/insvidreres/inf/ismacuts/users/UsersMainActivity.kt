package cat.insvidreres.inf.ismacuts.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import cat.insvidreres.inf.ismacuts.databinding.ActivityUsersMainBinding

class UsersMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
    }
}