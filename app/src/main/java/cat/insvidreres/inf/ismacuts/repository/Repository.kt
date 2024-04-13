package cat.insvidreres.inf.ismacuts.repository

import android.net.Uri
import android.util.Log
import cat.insvidreres.inf.ismacuts.admins.home.AdminBooking
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.users.Booking
import cat.insvidreres.inf.ismacuts.users.booking.Days
import cat.insvidreres.inf.ismacuts.users.booking.Hour
import cat.insvidreres.inf.ismacuts.users.home.Product
import cat.insvidreres.inf.ismacuts.users.home.Service
import cat.insvidreres.inf.ismacuts.utils.ErrorHandler
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Repository : ErrorHandler {

    companion object {
        private lateinit var storageRef: StorageReference
        var recyclerList = mutableListOf<User>()
        private val SALT: String = "+isma1234~$"
        var daysList = mutableListOf<Days>()
        var hoursList = mutableListOf<Hour>()
        var professionalList = mutableListOf<Professional>()
        var servicesList = mutableListOf<Service>()
        var productsList = mutableListOf<Product>()
        var usersList = mutableListOf<User>()
        var bookngsList = mutableListOf<Booking>()
        var adminBookingsList = mutableListOf<AdminBooking>()

        fun insertUser(user: User) {
            user.password = encryptPassword(user.password)

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        addItemToCollection(
                            "users", hashMapOf(
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

        //TODO update signIn method so that it detects somehow when to insert into users and when into professionals
        fun signIn(email: String, password: String, onComplete: (String) -> Unit) {
            getProfessionals("") {
                try {
                    if (professionalList.any { it.email == email }) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnFailureListener { e ->
                                Log.d("login failure", e.message.toString())
                            }
                        println("IT WAS professional $email")
                        onComplete("PROFESSIONAL")
                    } else {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnFailureListener { e ->
                                Log.d("login failure", e.message.toString())
                            }
                        println("IT WAS user $email")
                        onComplete("USER")
                    }

                } catch (e: Exception) {
                    Log.e("Error signIn", e.message.toString())
                } finally {
                    println("PROFESSIONALSLIST  $professionalList")
                }

            }
        }

        private fun addItemToCollection(collection: String, item: Any, docName: String = "") {
            val db = Firebase.firestore

            if (item != null && docName == "") {
                db.collection(collection).add(item).addOnFailureListener { e ->
                    Log.d("Firestore addItemToCollection", e.message.toString())
                }
            } else if (item != null && docName != "") {
                db.collection(collection).document(docName).set(item).addOnFailureListener { e ->
                    Log.d("Firestore addItemToCollection", e.message.toString())
                }
            } else {
                Log.d("Firestore else", "Wtf is this shit not working")
            }
        }

        fun addUsersToList(onComplete: () -> Unit) {
            val db = Firebase.firestore

            db.collection("users").get().addOnSuccessListener {
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
            }.addOnFailureListener { e ->
                Log.w("Firestore", e.message.toString())
            }
        }

        //Get professionals and their hours available
        fun getProfessionals(service: String, onComplete: () -> Unit) {
            val db = Firebase.firestore

            professionalList.clear()

            GlobalScope.launch(Dispatchers.IO) {
                if (!service.isNullOrEmpty()) {
                    db.collection("professionals").whereArrayContains("services", service).get()
                        .addOnSuccessListener {
                            for (professional in it) {
                                var appHourArray = mutableListOf<Hour>()
                                val appointmentArrayString =
                                    professional.data["appointments"] as MutableList<String>

                                for (hour in appointmentArrayString) {
                                    appHourArray.add(Hour(hour))
                                }

                                val pro = Professional(
                                    professional.data["name"].toString(),
                                    professional.data["email"].toString(),
                                    professional.data["password"].toString(),
                                    professional.data["reviews"] as MutableList<Number>,
                                    professional.data["services"] as MutableList<String>,
                                    appHourArray,
                                    professional.data["updatedTime"] as Number,
                                    "",
                                    professional.data["img"].toString(),
                                )
                                professionalList.add(pro)
                                println("Professionals list: $professionalList")
                            }

                            onComplete()
                        }.addOnFailureListener {
                            println("Error getting the professionals!!! $it")
                        }
                } else {
                    db.collection("professionals").get()
                        .addOnSuccessListener {
                            for (professional in it) {
                                var appHourArray = mutableListOf<Hour>()
                                val appointmentArrayString =
                                    professional.data["appointments"] as MutableList<String>

                                for (hour in appointmentArrayString) {
                                    appHourArray.add(Hour(hour))
                                }

                                val pro = Professional(
                                    professional.data["name"].toString(),
                                    professional.data["email"].toString(),
                                    professional.data["password"].toString(),
                                    professional.data["reviews"] as MutableList<Number>,
                                    professional.data["services"] as MutableList<String>,
                                    appHourArray,
                                    professional.data["updatedTime"] as Number,
                                    "",
                                    professional.data["img"].toString(),
                                )
                                professionalList.add(pro)
                                println("Professionals list: $professionalList")
                            }

                            onComplete()
                        }.addOnFailureListener {
                            println("Error getting the professionals!!! $it")
                        }
                }

            }
        }

        fun updateProfessionalAvailableHours(
            name: String, valueToDelete: String, onComplete: () -> Unit
        ) {
            val db = Firebase.firestore

            GlobalScope.launch(Dispatchers.IO) {
                db.collection("professionals").whereEqualTo("name", name).get()
                    .addOnSuccessListener {
                        for (professional in it) {
                            val array = professional.data["appointments"] as MutableList<String>
                            array.remove(valueToDelete)

                            db.collection("professionals").document(professional.id)
                                .update("appointments", array).addOnSuccessListener {

                                    hoursList.remove(Hour(valueToDelete))
                                    onComplete()
                                    println("Professional ${professional.data["name"]} hours updated")
                                }.addOnFailureListener {
                                    println("ERROR Updating the values  |  $it")
                                }
                        }
                    }.addOnFailureListener {
                        println("Error getting the document  |  $it")
                    }
            }

        }

        fun resetAllProfessionalsHours(onComplete: () -> Unit) {
            val db = Firebase.firestore

            GlobalScope.launch(Dispatchers.IO) {
                val currentTime = Calendar.getInstance().timeInMillis
                val dayStartTime = getDayStartTime(currentTime)

                var hoursReset = getHours()

                db.collection("professionals").get().addOnSuccessListener { querySnapshot ->
                    for (professional in querySnapshot) {
                        // Check if it's a new day since the last update
                        val updatedTime =
                            professional.get("updatedTime")?.toString()?.toLong() ?: 0
                        if (updatedTime < dayStartTime && updatedTime.toInt() != 0 && hoursReset.isNotEmpty()) {
                            professional.data["appointments"] = hoursReset

                            db.collection("professionals").document(professional.id).update(
                                "appointments", hoursReset, "updatedTime", currentTime
                            )
                        } else {
                            println("professional ${professional.data["name"]} has already been updated")
                        }
                    }
                    onComplete()
                }
            }
        }

        fun getDayStartTime(timestamp: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

        //Helper fun to resetAllProfessionalHours
        suspend fun getHours(): List<String> = suspendCoroutine { continuation ->
            val db = Firebase.firestore

            db.collection("hours").document("always").get()
                .addOnSuccessListener { documentSnapshot ->
                    val hours = documentSnapshot.data?.get("hours") as List<String>?
                    val hoursList = hours?.map { it } ?: emptyList()
                    if (hoursList.isNotEmpty()) {
                        println("Array of hours is not empty insane  | $hoursList")
                    } else {
                        println("Array of hours is empty gggggggggggggggggg  | $hoursList")
                    }
                    continuation.resume(hoursList)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }


        fun getHours(name: String, onComplete: () -> Unit) {
            val db = Firebase.firestore
            hoursList.clear()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    db.collection("professionals").whereEqualTo("name", name).get()
                        .addOnSuccessListener { list ->
                            for (professional in list) {
                                val hours = professional.data["appointments"] as MutableList<String>
                                if (hours != null) {
                                    hours.sort()

                                    for (i in hours) {
                                        hoursList.add(Hour(i))
                                    }
                                    onComplete()
                                    println("Hours list: $hoursList for $name")
                                } else {
                                    println("No hours data found")
                                }
                            }
                        }
                    onComplete()

                } catch (e: Exception) {
                    println("Error fetching hours data: $e")
                }
            }
        }

        fun getProducts(serviceType: String, onComplete: () -> Unit) {
            val db = Firebase.firestore
            productsList.clear()

            GlobalScope.launch(Dispatchers.IO) {
                if (serviceType.isEmpty()) {
                    db.collection("services").document("always").get().addOnSuccessListener {
                        val products = it.data?.get("services") as MutableList<Map<String, Any>>
                        for (service in products) {
                            productsList.add(
                                Product(
                                    service["name"].toString(),
                                    service["img"].toString(),
                                    service["serviceType"].toString(),
                                )
                            )
                        }

                        onComplete()
                    }
                } else {
                    db.collection("services").document("always").get().addOnSuccessListener {
                        val products = it.data?.get("services") as MutableList<Map<String, Any>>
                        if (products != null) {
                            for (service in products) {
                                if (service["serviceType"] as String == serviceType) {
                                    productsList.add(
                                        Product(
                                            service["name"] as String,
                                            service["img"] as String,
                                            service["serviceType"] as String,
                                        )
                                    )
                                }
                            }
                        }
                        onComplete()
                        println("ProductsList: $productsList")
                    }
                }
            }
        }

        fun getServices(onComplete: () -> Unit) {
            val db = Firebase.firestore
            servicesList.clear()

            GlobalScope.launch(Dispatchers.IO) {
                db.collection("services").document("servicesTypes").get().addOnSuccessListener {
                    val services = it.data?.get("services") as MutableList<Map<String, Any>>

                    for (service in services) {
                        servicesList.add(
                            Service(
                                service["name"].toString(),
                                service["src"].toString().lowercase(Locale.ROOT)
                                    .replace(" ", "_"),
                                service["serviceType"].toString()
                            )
                        )
                    }

                    onComplete()
                    println("ServicesList: $servicesList")
                }
            }
        }

        fun insertBooking(
            booking: Booking, onComplete: () -> Unit, onError: (error: String) -> Unit
        ) {
            val db = Firebase.firestore

            //TODO get username with user email

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val bookMap = mutableMapOf<String, Any>()
                    bookMap["userEmail"] = booking.userEmail
                    bookMap["product"] = booking.product
                    bookMap["professionalEmail"] = booking.professionalEmail
                    bookMap["day"] = booking.days
                    bookMap["hour"] = booking.hour

                    db.collection("bookings").document(booking.professionalEmail).get()
                        .addOnSuccessListener {
                            val existingBookings = it.data
                            //If test is null means document doesn't exist
                            //Returns the fields of the document as a Map or null if the document doesn't exist
                            if (existingBookings != null && existingBookings.containsKey("bookings")) {
                                val bookings =
                                    existingBookings["bookings"] as MutableList<Map<String, Any>>
                                bookings.add(bookMap)

                                db.collection("bookings").document(booking.professionalEmail)
                                    .update("bookings", bookings).addOnSuccessListener {
                                        println("Insert done, the document already EXISTED!!")
                                    }.addOnFailureListener {
                                        println("Error inserting, whilst the document existed  |  ${it.message}")
                                    }

                            } else {
                                val newBookings = mutableMapOf<String, Any>()
                                newBookings["bookings"] = mutableListOf(bookMap)

                                db.collection("bookings").document(booking.professionalEmail)
                                    .set(newBookings).addOnSuccessListener {
                                        println("Inserted correctly. The document dod not exist")
                                    }.addOnFailureListener {
                                        println("Error inserting, whilst the document dod not exist  |  ${it.message}")
                                    }
                            }
                        }.addOnFailureListener {
                            println("Error fetching document: ${it.message}")
                        }

                    onComplete()
                } catch (e: Exception) {
                    println("ERROR in insertBooking wtf  |  ${e.message}")
                    onError(e.message.toString())
                }
            }
        }

        fun getBookings(email: String, isProfessional: Boolean, onComplete: () -> Unit) {

            val db = Firebase.firestore

            GlobalScope.launch(Dispatchers.IO) {
                if (email.isNotBlank()) {
                    try {
                        db.collection("bookings").document(email).get()
                            .addOnSuccessListener { documentSnapshot ->
                                bookngsList.clear()
                                usersList.clear()
                                professionalList.clear()
                                val data = documentSnapshot.data
                                if (data != null && data.containsKey("bookings")) {
                                    val bookings =
                                        data["bookings"] as? MutableList<Map<String, Any>>

                                    println("bookings for $email | $bookings")

                                    bookings?.forEach { item ->
                                        val userEmail = item["userEmail"].toString()

                                        val productData = item["product"] as? Map<*, *>
                                        val product = productData?.let {
                                            Product(
                                                it["name"] as? String ?: "",
                                                it["serviceType"] as? String ?: "",
                                                it["img"] as? String ?: ""
                                            )
                                        }

                                        val professionalEmail = item["professionalEmail"].toString()

                                        val dayData = item["day"] as? Map<*, *>
                                        val day = dayData?.let {
                                            Days(
                                                it["day"] as? Number ?: 0,
                                                it["dayOfWeek"] as? String ?: ""
                                            )
                                        }

                                        val hourData = item["hour"] as? Map<*, *>
                                        val hour = hourData?.let {
                                            Hour(
                                                it["hour"] as? String ?: ""
                                            )
                                        }

                                        getDetailsWithEmail(userEmail, !isProfessional) { //To get user data
                                            if (product != null && day != null && hour != null) {
                                                println("usersList $usersList")
                                                if (isProfessional) {
                                                    for (i in usersList) {
                                                        adminBookingsList.add(
                                                            AdminBooking(
                                                                i.username,
                                                                day,
                                                                hour.hour,
                                                                product.name
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    bookngsList.add(
                                                        Booking(
                                                            userEmail,
                                                            product,
                                                            professionalEmail,
                                                            day,
                                                            hour
                                                        )
                                                    )
                                                }
                                                onComplete()
                                            } else {
                                                println("One of the elements is missing product or day or hour")
                                            }
                                        }
                                    }
                                } else {
                                    println("No bookings found for professional with email: $email")
                                }
                            }
                    } catch (e: Exception) {
                        println("Error getting bookings: ${e.message}")
                    } finally {
                        println("adminBookings array $adminBookingsList")
                    }
                } else {
                    println("Email is blank or null. $email")
                }
            }
        }

        fun getDetailsWithEmail(email: String, isProfessional: Boolean, onComplete: () -> Unit) {
            val db = Firebase.firestore

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (isProfessional == false) {
                        db.collection("users").whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener {
                                usersList.clear()
                                for (user in it) {
                                    println("is email same as gotten? ${email == user["email"] as String}")
                                    usersList.add(
                                        User(
                                            user["username"] as String,
                                            user["email"] as String,
                                            user["password"] as String,
                                            user["admin"] as Boolean,
                                            "",
                                            user["img"] as String
                                        )
                                    )
                                }
                                onComplete()
                            }
                    } else {
                        db.collection("professionals").whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener {
                                professionalList.clear()
                                for (professional in it) {
                                    var appHourArray = mutableListOf<Hour>()
                                    val appointmentArrayString =
                                        professional.data["appointments"] as MutableList<String>

                                    for (hour in appointmentArrayString) {
                                        appHourArray.add(Hour(hour))
                                    }

                                    val pro = Professional(
                                        professional.data["name"].toString(),
                                        professional.data["email"].toString(),
                                        professional.data["password"].toString(),
                                        professional.data["reviews"] as MutableList<Number>,
                                        professional.data["services"] as MutableList<String>,
                                        appHourArray,
                                        professional.data["updatedTime"] as Number,
                                        "",
                                        professional.data["img"].toString(),
                                    )
                                    professionalList.add(pro)
                                    println("Professionals list: $professionalList")
                                }
                                onComplete()
                            }
                    }
                } catch (e: Exception) {
                    println("getDetailsWitEmail catch | ${e.message}")
                }
            }
        }


        fun getDays(onComplete: () -> Unit) {
            daysList.clear()

            GlobalScope.launch(Dispatchers.IO) {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val currentMonth = Calendar.getInstance()
                    .get(Calendar.MONTH) + 1 // Months are 0-indexed in Calendar

                val yearData = getDatesFromFirestore(currentYear)

                if (yearData != null) {
                    val currentMonthData = yearData.filter {
                        val month = it["month"] as String
                        val monthCalendar = Calendar.getInstance()
                        monthCalendar.time =
                            SimpleDateFormat("MMMM", Locale.ENGLISH).parse(month) ?: Date()

                        monthCalendar.get(Calendar.MONTH) + 1 == currentMonth
                    }

                    currentMonthData.forEach { day ->
                        val dayNumber = day["dayNumber"] as Number
                        val dayOfWeek = day["dayName"] as String

                        daysList.add(Days(dayNumber, dayOfWeek))
                    }

                    onComplete()
                    println("Data for current month: $currentMonthData")
                } else {
                    println("No data found for the current year")
                }
            }
        }


        fun readFromFirebaseStorage(imageUri: Uri?) {

            val db = Firebase.firestore

            db.collection("users").get().addOnSuccessListener {
                for (i in it) {
//                        imagesList.add(i.data["img"].toString())
                }
            }

            storageRef = FirebaseStorage.getInstance().reference
            val testReference = storageRef.child("Images/Wireframe-1.jpg")

            val ONE_MEGABYTE: Long = 1024 * 1024

            testReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->

            }.addOnFailureListener { e ->
                Log.d("ERROR Reading storage", e.message.toString())
            }
        }

        fun updateUser(oldUser: User, newUser: User, imgUri: Uri?, onComplete: () -> Unit) {
            val db = Firebase.firestore
            var imgUrl: String = ""

            FirebaseAuth.getInstance().currentUser?.email?.let { Log.i("Current User email?", it) }

            storageRef = FirebaseStorage.getInstance().reference.child("/Images")

            if (oldUser.email != newUser.email && oldUser.img != "https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fplaceholder_pfp.jpg?alt=media&token=51075713-0a43-48a0-922e-426d75f85552") {
                storageRef.child(oldUser.email + "-pfp").delete()
            }
            storageRef = storageRef.child(newUser.email + "-pfp")

            imgUri?.let {
                storageRef.putFile(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            imgUrl = uri.toString()
                            newUser.img = imgUrl

                            Log.i("newUser.img", newUser.img)

                            db.collection("users").whereEqualTo("email", oldUser.email).get()
                                .addOnSuccessListener { docs ->
                                    for (doc in docs) {
                                        db.collection("users").document(doc.id).update(
                                            hashMapOf<String, Any>(
                                                "username" to newUser.username,
                                                "email" to newUser.email,
                                                "password" to newUser.password,
                                                "admin" to newUser.admin,
                                                "img" to newUser.img,
                                                "id" to newUser.id
                                            )
                                        ).addOnSuccessListener {
                                            if (oldUser.email != newUser.email) {
                                                val credential: AuthCredential =
                                                    EmailAuthProvider.getCredential(
                                                        oldUser.email, oldUser.password
                                                    )
                                                FirebaseAuth.getInstance().currentUser?.reauthenticate(
                                                    credential
                                                )?.addOnSuccessListener { task ->
                                                    FirebaseAuth.getInstance().currentUser?.verifyBeforeUpdateEmail(
                                                        newUser.email
                                                    )?.addOnSuccessListener {
                                                        Log.i(
                                                            "email update",
                                                            "Email updated successfully"
                                                        )

                                                        val credential: AuthCredential =
                                                            EmailAuthProvider.getCredential(
                                                                oldUser.email,
                                                                oldUser.password
                                                            )
                                                        FirebaseAuth.getInstance().currentUser?.reauthenticate(
                                                            credential
                                                        )?.addOnSuccessListener {
                                                            FirebaseAuth.getInstance().currentUser?.updatePassword(
                                                                newUser.password
                                                            )
                                                                ?.addOnSuccessListener {
                                                                    Log.i(
                                                                        "Passw updated",
                                                                        "Pass updates successfully"
                                                                    )
                                                                }
                                                        }



                                                        Log.i(
                                                            "Credentials",
                                                            "Auth credentials updated? check"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    Log.e(
                                        "Error in getting the users (update)",
                                        exception.message.toString()
                                    )
                                }
                        }.addOnFailureListener { exception ->
                            Log.e("url download error", exception.message.toString())
                        }
                    } else {
                        Log.e("storage upload", "Error uploading file gg")
                    }
                }
            }
        }

        fun deleteUser(user: User, onComplete: () -> Unit) {
            FirebaseAuth.getInstance().signOut()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onComplete()
                        } else {
                            // Failed to delete user
                            Log.e("deleteUser", "Failed to delete user: ${task.exception}")
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.e("deleteUser", "Failed to sign in: ${exception.message}")
                }

            val db = Firebase.firestore

            db.collection("users").whereEqualTo("email", user.email)
                .whereEqualTo("username", user.username).get().addOnSuccessListener { docs ->
                    for (doc in docs) {
                        db.collection("users").document(doc.id).delete()
                    }
                    onComplete()
                }
        }

        private fun encryptPassword(password: String): String {
            val saltedPW = password + SALT
            val digest = MessageDigest.getInstance("SHA-256")
            val hashedBytes = digest.digest(saltedPW.toByteArray(Charsets.UTF_8))
            return Base64.getEncoder().encodeToString(hashedBytes)
        }

        //To update the days collection

        suspend fun getLastDocumentYear(): Int? {
            val firestore = Firebase.firestore
            return try {
                val querySnapshot = firestore.collection("days").orderBy(
                    com.google.firebase.firestore.FieldPath.documentId(),
                    Query.Direction.DESCENDING
                ).limit(1).get().await()

                querySnapshot.documents.firstOrNull()?.id?.toIntOrNull()
            } catch (e: Exception) {
                println("Error getting last document year from Firestore: $e")
                null
            }
        }

        fun generateDaysOfYear(year: Int): List<Map<String, Any>> {
            val daysOfYear = mutableListOf<Map<String, Any>>()
            val calendar = Calendar.getInstance()

            for (month in Calendar.JANUARY..Calendar.DECEMBER) {
                calendar.set(year, month, 1)

                val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)

                val numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

                val monthDays = mutableListOf<Map<String, Any>>()

                for (dayOfMonth in 1..numDaysInMonth) {
                    calendar.set(year, month, dayOfMonth)
                    val date = calendar.time
                    val dayNumber = SimpleDateFormat("d", Locale.getDefault()).format(date).toInt()
                    val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)

                    val dayMap = mapOf(
                        "dayNumber" to dayNumber, "dayName" to dayName
                    )

                    monthDays.add(dayMap)
                }

                val monthMap = mapOf(
                    "month" to monthName, "days" to monthDays
                )

                daysOfYear.add(monthMap)
            }

            return daysOfYear
        }

        suspend fun saveDaysToFirestore(year: Int, days: List<Map<String, Any>>) {
            val firestore = Firebase.firestore
            try {
                val daysCollection = firestore.collection("days")
                val documentRef = daysCollection.document(year.toString())

                documentRef.set(mapOf("data" to days)).await()

                println("Days for year $year saved successfully to Firestore.")
            } catch (e: Exception) {
                println("Error saving days to Firestore: $e")
            }
        }

        suspend fun getDatesFromFirestore(year: Int): List<Map<String, Any>> {
            val firestore = Firebase.firestore
            return try {
                val documentSnapshot =
                    firestore.collection("days").document(year.toString()).get().await()

                val data =
                    documentSnapshot.data?.get("data") as? List<Map<String, Any>> ?: emptyList()

                data.flatMap { monthData ->
                    val month = monthData["month"] as? String ?: ""
                    val days = monthData["days"] as? List<Map<String, Any>> ?: emptyList()
                    days.map { day ->
                        val dayNumber = day["dayNumber"] as? Number
                            ?: -1 // Default value or handle appropriately
                        val dayName =
                            day["dayName"] as? String ?: "" // Default value or handle appropriately
                        mapOf(
                            "month" to month, "dayNumber" to dayNumber, "dayName" to dayName
                        )
                    }
                }
            } catch (e: Exception) {
                println("Error getting dates from Firestore: $e")
                emptyList()
            }
        }


    }

}