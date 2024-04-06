package cat.insvidreres.inf.ismacuts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cat.insvidreres.inf.ismacuts.main_screen.MainScreenActivity
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, UsersMainActivity::class.java))
    }
}