package cat.insvidreres.inf.ismacuts.model

data class Professional(
    val name: String,
    val email: String,
    val password: String,
    val reviews: List<Number> = emptyList(),
    val services: List<String> = emptyList(),
    //TODO List of appointments, similar to the dates
//    val appointments: List<>
    var id: String = "",
    var img: String = "https://firebasestorage.googleapis.com/v0/b/ismacuts-a6d41.appspot.com/o/Images%2Fplaceholder_pfp.jpg?alt=media&token=51075713-0a43-48a0-922e-426d75f85552"
)
