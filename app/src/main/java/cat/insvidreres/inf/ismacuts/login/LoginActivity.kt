package cat.insvidreres.inf.ismacuts.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import cat.insvidreres.inf.ismacuts.recycler.RecyclerActivity
import cat.insvidreres.inf.ismacuts.databinding.ActivityLoginBinding
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel
import cat.insvidreres.inf.ismacuts.users.UsersMainActivity
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.bumptech.glide.Glide
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

        Glide.with(binding.loginLogoIV.context)
            .load("https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fbarber_logo-removebg-preview.png?alt=media&token=20d7ae70-c3b9-4b81-958f-63a24c154464")
            .into(binding.loginLogoIV)

        binding.loginPageLoginButton.setOnClickListener {
            if (binding.loginEmailET.text.isNotEmpty() &&
                !binding.passwordT.text.isNullOrEmpty()
            ) {

                val email = binding.loginEmailET.text.toString()
                val password = binding.passwordT.text.toString()

                if (viewModel.loginWithEmailAndPw(email, password)) {
                    goToMainActivity(this, email)
                }
            }
        }

    }


    private fun goToMainActivity(context: Context, email: String) {
        val intent = Intent(context, UsersMainActivity::class.java)
        intent.putExtra("userEmail", email)
        startActivity(intent)
    }


}