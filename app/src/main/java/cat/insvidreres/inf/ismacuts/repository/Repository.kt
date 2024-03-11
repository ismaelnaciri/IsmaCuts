package cat.insvidreres.inf.ismacuts.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import cat.insvidreres.inf.ismacuts.model.Beardtrim
import cat.insvidreres.inf.ismacuts.model.Haircut
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class Repository: ErrorHandler {

    companion object {
        var haircuts: LiveData<List<Haircut>>? = null

        fun insertUser(user: User) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        addItemToCollection(
                            "users",
                            hashMapOf(
                                "username" to user.username,
                                "email" to user.email,
                                "password" to user.password,
                                "admin" to user.admin,
                            )
                        )
                    }
                }

            Log.d("User fields test", "email | ${user.email}  |  password ${user.password}")
        }

        fun signIn(email: String, password: String) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnFailureListener { e ->
                    Log.d("login else", e.message.toString())
                }
        }

        fun insertHaircut(context: Context, haircut: Haircut) {

        }

        fun insertBeardtrim(context: Context, beardtrim: Beardtrim) {

        }

        private fun addItemToCollection(collection: String, item: Any, docName: String = "") {
            val db = Firebase.firestore

            if (item != null && docName == "") {
                db.collection(collection)
                    .add(item)
                    .addOnFailureListener {e ->
                        Log.d("Firestore addItemToCollection", e.message.toString())
                    }
            } else if (item != null && docName != "") {
                db.collection(collection)
                    .document(docName)
                    .set(item)
                    .addOnFailureListener { e ->
                        Log.d("Firestore addItemToCollection", e.message.toString())
                    }
            } else {
                Log.d("Firestore else", "Wtf is this shit not working")
            }
        }
    }

}