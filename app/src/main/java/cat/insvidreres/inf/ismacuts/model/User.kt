package cat.insvidreres.inf.ismacuts.model

import java.io.Serializable

data class User(
    val username: String,
    val email: String,
    var password: String,
//    val dateOfBirth: String,
    val admin: Boolean = false,
    val id: String = "",
    var img: String = "https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fplaceholder_pfp.jpg?alt=media&token=51075713-0a43-48a0-922e-426d75f85552"
) : Serializable
