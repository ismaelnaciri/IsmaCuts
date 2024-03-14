package cat.insvidreres.inf.ismacuts.model

import java.io.Serializable

data class User(
    val username: String,
    val email: String,
    val password: String,
//    val dateOfBirth: String,
    val admin: Boolean = false,
    val id: String = "",
    val img: String = ""
) : Serializable
