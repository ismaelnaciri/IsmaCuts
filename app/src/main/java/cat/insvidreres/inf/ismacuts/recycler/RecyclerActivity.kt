package cat.insvidreres.inf.ismacuts.recycler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.UserSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.ActivityLoginBinding
import cat.insvidreres.inf.ismacuts.databinding.ActivityRecyclerBinding
import cat.insvidreres.inf.ismacuts.databinding.UserItemEntregaBinding
import cat.insvidreres.inf.ismacuts.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RecyclerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: RecyclerAdapter

    private lateinit var userBinding: UserItemEntregaBinding

    private val viewModel: RecyclerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.firestore

        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        userBinding = UserItemEntregaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usersRecyclerView.setHasFixedSize(true)
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)

        val userAdapter = RecyclerAdapter(this, emptyList(), userBinding) { selectedItem ->

            Toast.makeText(
                this,
                "Username: " + selectedItem.username + " | Email: " + selectedItem.email,
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, SelectedUser::class.java)
            intent.putExtra("user", selectedItem)
            startActivity(intent)
        }

        viewModel.loadUsers()
        viewModel.users.observe(this) { usersList ->
            userAdapter.usersList = usersList
            binding.usersRecyclerView.adapter = userAdapter
        }

        binding.refreshButton.setOnClickListener {
            viewModel.loadUsers()
        }
    }
}