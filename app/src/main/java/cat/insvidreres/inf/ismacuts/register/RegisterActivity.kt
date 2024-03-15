package cat.insvidreres.inf.ismacuts.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ActivityRegisterBinding
import cat.insvidreres.inf.ismacuts.login.LoginActivity
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity(), ErrorHandler {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics

    private val viewModel: RegisterViewModel by viewModels()


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerPageSignUpButton.setOnClickListener {
            if (binding.registerUsernameET.text.toString() !== ""
                && binding.registerEmailET.text.toString() !== ""
                && binding.registerPasswordET.text.toString() !== ""
            ) {
                val email = binding.registerEmailET.text.toString()
                val password = binding.registerPasswordET.text.toString()
                val username = binding.registerUsernameET.text.toString()

                viewModel.createAccount(User(username, email, password))
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                goToLogin(this)
            }
        }
    }

    private fun goToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}