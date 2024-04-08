package cat.insvidreres.inf.ismacuts.users

import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.users.booking.Days
import cat.insvidreres.inf.ismacuts.users.booking.Hour
import cat.insvidreres.inf.ismacuts.users.home.Product
import cat.insvidreres.inf.ismacuts.users.home.Service

data class Booking(
    var userEmail: String,
    var product: Product,
    var professionalEmail: String,
    var days: Days,
    var hour: Hour
)
