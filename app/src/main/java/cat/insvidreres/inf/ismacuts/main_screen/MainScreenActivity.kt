package cat.insvidreres.inf.ismacuts.main_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ActivityMainScreenBinding
import cat.insvidreres.inf.ismacuts.login.LoginActivity
import cat.insvidreres.inf.ismacuts.register.RegisterActivity
import cat.insvidreres.inf.ismacuts.repository.Repository
import cat.insvidreres.inf.ismacuts.users.booking.Days
import java.util.*
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        firestore = Firebase.firestore
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
        load("https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fbarber_logo-removebg-preview.png?alt=media&token=20d7ae70-c3b9-4b81-958f-63a24c154464")
            .into(binding.mainScreenLogoIV)


        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        GlobalScope.launch(Dispatchers.Default) {
            val lastDocumentYear = Repository.getLastDocumentYear()

            if (lastDocumentYear == null || currentYear > lastDocumentYear) {
                val daysOfYear = Repository.generateDaysOfYear(currentYear)
                Repository.saveDaysToFirestore(currentYear, daysOfYear)
            } else {
                println("Current year $currentYear is not greater than the last document year $lastDocumentYear in Firestore.")
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            Repository.resetAllProfessionalsHours {
                println("Professional hour reset completed!!")
            }
        }
    }
}