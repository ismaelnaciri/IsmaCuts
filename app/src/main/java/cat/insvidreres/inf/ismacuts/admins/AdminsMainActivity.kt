package cat.insvidreres.inf.ismacuts.admins

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cat.insvidreres.inf.ismacuts.R

class AdminsMainActivity : AppCompatActivity() {

    //TODO Binding of main activity, navHost in xml, fragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        enableEdgeToEdge()
        setContentView(R.layout.activity_admins_main)

    }
}