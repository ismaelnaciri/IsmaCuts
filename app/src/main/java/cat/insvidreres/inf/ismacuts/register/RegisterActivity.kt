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

    //TODO -> Move this code to ItemSelected Of Recycler Activity
//    private var imageUri: Uri? = null
//    private val resultLauncher = registerForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) {
//        imageUri = it
//        binding.userImageEdit.setImageURI(imageUri)
//    }

    //binding.imageView.setOnClickListener {
    //            resultLauncher.launch("image/*")
    //        }

    /*
    <ImageView
                android:id="@+id/imageView"
                android:layout_width="0dp"
                android:layout_height="0dp"
                android:scaleType="fitXY"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.5"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/vector" />


    private fun uploadImage() {
        storageRef = storageRef.child("Images/")
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("users")
                            .document()
                            .add(map)
                            .addOnCompleteListener { firestoreTask ->

                            if (firestoreTask.isSuccessful){
                                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            }else{
                                Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()

                            }
                            binding.imageView.setImageResource(R.drawable.vector)

                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.imageView.setImageResource(R.drawable.vector)
                }
            }
        }
    }
    */

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

                goToLogin(this)
            }
        }
    }

    private fun goToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }
}