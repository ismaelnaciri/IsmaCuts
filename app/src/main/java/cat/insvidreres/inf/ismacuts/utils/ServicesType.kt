package cat.insvidreres.inf.ismacuts.utils

enum class ServicesType(private val service: String) {
    HAIRCUT("Haircut"),
    BEARD_TRIM("Beard Trim"),
    SKIN_CARE("Skin Care");

    override fun toString(): String {
        return service
    }
}