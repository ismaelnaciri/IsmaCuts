package cat.insvidreres.inf.ismacuts.repository

import android.content.Context
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Repository : ErrorHandler {

    companion object {
        var haircuts: LiveData<List<Haircut>>? = null
        private lateinit var storageRef: StorageReference
        var recyclerList = mutableListOf<User>()

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
                                "img" to user.img
                            )
                        )
                    }
                }

            Log.d("User fields test", "email | ${user.email}  |  password ${user.password}")
        }

        fun signIn(email: String, password: String) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnFailureListener { e ->
                    Log.d("login failure", e.message.toString())
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
                    .addOnFailureListener { e ->
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

        fun addUsersToList(onComplete: () -> Unit) {
            val db = Firebase.firestore

            db.collection("users")
                .get()
                .addOnSuccessListener {
                    val list = mutableListOf<User>()
                    for (i in it) {
                        val user = User(
                                i.data["username"].toString(),
                                i.data["email"].toString(),
                                i.data["password"].toString(),
                                i.data["admin"].toString().toBoolean(),
                                "",
                                i.data["img"].toString()
                        )
                        list.add(user)
                    }

                    recyclerList = list
                    onComplete()
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", e.message.toString())
                }
        }


        fun readFromFirebaseStorage(imageUri: Uri?) {


            val db = Firebase.firestore

            db.collection("users")
                .get()
                .addOnSuccessListener {
                    for (i in it) {
//                        imagesList.add(i.data["img"].toString())
                    }
                }

            storageRef = FirebaseStorage.getInstance().reference
            val testReference = storageRef.child("Images/Wireframe-1.jpg")

            val ONE_MEGABYTE: Long = 1024 * 1024

            testReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { data ->

                }
                .addOnFailureListener { e ->
                    Log.d("ERROR Reading storage", e.message.toString())
                }
        }

        fun updateUser(oldUser: User, newUser: User, imgUri: Uri?, onComplete: () -> Unit) {
            val db = Firebase.firestore
            var imgUrl: String = ""

            storageRef = FirebaseStorage.getInstance().reference.child("/Images")

            if (oldUser.email != newUser.email
                && oldUser.img != "https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fplaceholder_pfp.jpg?alt=media&token=51075713-0a43-48a0-922e-426d75f85552") {
                storageRef.child(oldUser.email + "-pfp").delete()
            }
            storageRef = storageRef.child(newUser.email + "-pfp")

            imgUri?.let {
                storageRef.putFile(it).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {

                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            imgUrl = uri.toString()
                            newUser.img = imgUrl

                            Log.i("newUser.img", newUser.img)

                            db.collection("users")
                                .whereEqualTo("email", oldUser.email)
                                .get()
                                .addOnSuccessListener { docs ->
                                    for (doc in docs) {
                                        db.collection("users")
                                            .document(doc.id)
                                            .update(hashMapOf<String, Any>(
                                                "username" to newUser.username,
                                                "email" to newUser.email,
                                                "password" to newUser.password,
                                                "admin" to newUser.admin,
                                                "img" to newUser.img,
                                                "id" to newUser.id
                                            ))
                                            .addOnSuccessListener {
                                                if (oldUser.email != newUser.email) {
                                                    FirebaseAuth.getInstance().currentUser?.updateEmail(newUser.email)
                                                    FirebaseAuth.getInstance().currentUser?.updatePassword(newUser.password)

//                                                    FirebaseAuth.getInstance().
                                                }
                                            }
                                    }
                                    onComplete()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Error in getting the users (update)", exception.message.toString())
                                }
                        }
                            .addOnFailureListener { exception ->
                                Log.e("url download error", exception.message.toString())
                            }
                    } else {
                        Log.e("storage upload", "Error uploading file gg")
                    }
                }
            }
        }

        fun deleteUser(user: User, onComplete: () -> Unit) {
            val db = Firebase.firestore

            db.collection("users")
                .whereEqualTo("email", user.email)
                .whereEqualTo("username", user.username)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        db.collection("users").document(doc.id).delete()
                    }
                    onComplete()
                }
        }
    }

}