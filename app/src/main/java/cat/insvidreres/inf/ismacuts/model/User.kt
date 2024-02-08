package cat.insvidreres.inf.ismacuts.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val admin: Boolean,
)
