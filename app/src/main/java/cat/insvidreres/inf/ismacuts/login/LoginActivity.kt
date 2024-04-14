package cat.insvidreres.inf.ismacuts.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cat.insvidreres.inf.ismacuts.admins.AdminsMainActivity
import cat.insvidreres.inf.ismacuts.databinding.ActivityLoginBinding
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class LoginActivity : AppCompatActivity(), ErrorHandler {
    //    private val viewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        db = Firebase.firestore

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val password = sharedPreferences.getString("password", "")

            if (password != "") {
                binding.rememberPasswordCB.isChecked = true
                binding.passwordT.setText(password)
            }
        } catch (e: Exception) {
            println("Error getting user password: ${e.message}")
        }

//        Glide.with(binding.loginLogoIV.context)
//            .load("https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fbarber_logo-removebg-preview.png?alt=media&token=20d7ae70-c3b9-4b81-958f-63a24c154464")
//            .into(binding.loginLogoIV)

        binding.loginPageLoginButton.setOnClickListener {
            if (binding.loginEmailET.text.isNotEmpty() &&
                !binding.passwordT.text.isNullOrEmpty()
            ) {
                val email = binding.loginEmailET.text.toString().lowercase().trim().replace(" ", "")
                val password = binding.passwordT.text.toString().lowercase().trim().replace(" ", "")

                if (binding.rememberPasswordCB.isChecked) {
                    val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("password", password)
                    editor.apply()
                } else {
                    val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.remove("password")
                    editor.apply()
                }

                viewModel.loginWithEmailAndPw(email, password) { role ->
                    when (role) {
                        "USER" -> {
                            goToMainActivity(this, email, "USER")
                        }
                        "PROFESSIONAL" -> {
                            goToMainActivity(this, email, "PROFESSIONAL")
                        }
                    }
                }
            }
        }

    }


    private fun goToMainActivity(context: Context, email: String, role: String) {
        if (role == "USER") {
            val intent = Intent(context, UsersMainActivity::class.java)
            intent.putExtra("userEmail", email)
            startActivity(intent)
        } else if (role == "PROFESSIONAL") {
            val intent = Intent(context, AdminsMainActivity::class.java)
            intent.putExtra("adminEmail", email)
            startActivity(intent)
        }

    }


}