package cat.insvidreres.inf.ismacuts.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ActivityRegisterBinding
import cat.insvidreres.inf.ismacuts.login.LoginActivity
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.recycler.RecyclerActivity
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity(), ErrorHandler {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private final val RC_SIGN_IN: Int = 9001

    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerPageSignUpButton.setOnClickListener {
            if (binding.registerUsernameET.text.toString() !== ""
                && binding.registerEmailET.text.toString() !== ""
                && binding.passwordRegisterT.text.toString() !== ""
            )
            {
                val email = binding.registerEmailET.text.toString()
                val password = binding.passwordRegisterT.text.toString()
                val username = binding.registerUsernameET.text.toString()

                viewModel.createAccount(User(username, email, password))
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                goToLogin(this)
            }
        }

//        binding.googleSignInButton.setOnClickListener {
//            mAuth = FirebaseAuth.getInstance()
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("417580188805-2rnfeultah6ra1hirf7ghiigg0apmt3u.apps.googleusercontent.com")
//                .requestEmail()
//                .build()
//
//            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//            val googleSignInIntent = mGoogleSignInClient.signInIntent
//            startActivityForResult(googleSignInIntent, RC_SIGN_IN)
//        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Google sign in failed: ${e.message.toString()}", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        mAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, RecyclerActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun goToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}