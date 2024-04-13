package cat.insvidreres.inf.ismacuts.admins.home

import cat.insvidreres.inf.ismacuts.users.booking.Days

data class AdminBooking(
    var userName: String,
    val day: Days,
    var hour: String,
    var productName: String
)