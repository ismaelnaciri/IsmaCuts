package cat.insvidreres.inf.ismacuts.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import cat.insvidreres.inf.ismacuts.recycler.RecyclerActivity
import cat.insvidreres.inf.ismacuts.databinding.ActivityLoginBinding
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

        binding.loginPageLoginButton.setOnClickListener {
            if (binding.loginEmailET.text.isNotEmpty() &&
                !binding.passwordT.text.isNullOrEmpty())
            {

                val email = binding.loginEmailET.text.toString()
                val password = binding.passwordT.text.toString()

                if(viewModel.loginWithEmailAndPw(email, password)) {
                    goToMainActivity(this)
                }
            }
        }

    }


    private fun goToMainActivity(context: Context) {
        val intent = Intent(context, RecyclerActivity::class.java)
        startActivity(intent)
    }


}