package cat.insvidreres.inf.ismacuts.recycler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    private var imageUri: Uri? = null

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

                val intent = Intent(this, RecyclerActivity::class.java)
                startActivity(intent)
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
                        binding.adminSwitch.isChecked,
                        "",

                    ),
                    imageUri)
            }
        }

        binding.selectedUserIV.setOnClickListener {
            resultLauncher.launch("image/*")
        }

    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){

        imageUri = it
        binding.selectedUserIV.setImageURI(it)
    }

    private fun initValues(user: User, binding: ActivitySelectedUserBinding) {
        binding.usernameET.setText(user.username)
        binding.emailET.setText(user.email)
        binding.passwordET.setText(user.password)
        binding.adminSwitch.isChecked = (user.admin.toString() === "true")
        Glide.with(binding.selectedUserIV.context).load(user.img).into(binding.selectedUserIV)
    }
}