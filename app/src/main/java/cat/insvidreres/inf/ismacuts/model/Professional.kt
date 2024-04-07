package cat.insvidreres.inf.ismacuts.model

import cat.insvidreres.inf.ismacuts.users.booking.Hour

data class Professional(
    val name: String,
    val email: String,
    val password: String,
    val reviews: MutableList<Number> = mutableListOf<Number>(),
    val services: MutableList<String> = mutableListOf<String>(),
    val appointments: MutableList<Hour> = mutableListOf<Hour>(),
    val updatedTime: Number,
    var id: String = "",
    var img: String = "https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fplaceholder_pfp.jpg?alt=media&token=51075713-0a43-48a0-922e-426d75f85552"
)
