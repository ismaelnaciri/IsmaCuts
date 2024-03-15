package cat.insvidreres.inf.ismacuts.main_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ActivityMainScreenBinding
import cat.insvidreres.inf.ismacuts.login.LoginActivity
import cat.insvidreres.inf.ismacuts.register.RegisterActivity
import com.bumptech.glide.Glide

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainScreenLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.mainScreenRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        Glide.with(binding.mainScreenLogoIV.context).
        load("https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fbarber_logo.jpg?alt=media&token=4b7440b2-5329-4274-a332-e314896bd5d4")
            .into(binding.mainScreenLogoIV)
    }
}