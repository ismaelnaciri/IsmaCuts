package cat.insvidreres.inf.ismacuts.utils

enum class ServicesType(private val service: String) {
    HAIRCUT("Haircut"),
    BEARD_TRIM("Beard Trim"),
    NAIL_POLISH("Nail Polish");

    override fun toString(): String {
        return service
    }
}