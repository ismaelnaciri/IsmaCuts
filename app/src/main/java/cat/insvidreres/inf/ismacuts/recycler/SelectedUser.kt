package cat.insvidreres.inf.ismacuts.recycler

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.UserSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.ActivitySelectedUserBinding
import cat.insvidreres.inf.ismacuts.model.User
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class SelectedUser : AppCompatActivity() {
    private lateinit var binding: ActivitySelectedUserBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val viewModel: SelectedUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.firestore

        super.onCreate(savedInstanceState)
        binding = ActivitySelectedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedUser = intent.getSerializableExtra("user", User::class.java)
        if (receivedUser != null) {
            initValues(receivedUser, binding)
        }

        binding.deleteButton.setOnClickListener {
            if (receivedUser != null) {
                viewModel.deleteUser(receivedUser)
            }
        }

        binding.UpdateButton.setOnClickListener {
            if (receivedUser != null) {
                viewModel.updateUser(
                    receivedUser,
                    User(
                        binding.usernameET.text.toString(),
                        binding.emailET.text.toString(),
                        binding.passwordET.text.toString(),
                        binding.adminSwitch.isChecked
                    ))
            }
        }
        //TODO -> Get user from firestore and check if the values in the form are different than new ones
        //TODO -> Methods go to repository
//        db.collection("users").whereEqualTo("email", userSharedViewModel.user.value?.email).get().addOnSuccessListener { doc ->
        //          for (d in doc) {
        //          if(d.data["email"] === userSharedViewModel.user.value?.email
        //              && d.data["password"] === userSharedViewModel.user.value?.password) {
//        FirebaseAuth.getInstance().currentUser?.updatePassword(binding.passwordET.text.toString())
        //
        //              }
        //          }
        //        }
//        FirebaseAuth.getInstance().currentUser?.updateEmail(binding.emailET.text.toString())
//        db.collection("users").whereEqualTo("email", userSharedViewModel.user.value?.email)
//            .get()
    }

    private fun initValues(user: User, binding: ActivitySelectedUserBinding) {
        binding.usernameET.setText(user.username)
        binding.emailET.setText(user.email)
        binding.passwordET.setText(user.password)
        binding.adminSwitch.isChecked = (user.admin.toString() === "true")
        Glide.with(binding.selectedUserIV.context).load(user.img).into(binding.selectedUserIV)
    }
}