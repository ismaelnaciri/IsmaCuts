package cat.insvidreres.inf.ismacuts.admins.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.repository.Repository
import cat.insvidreres.inf.ismacuts.users.Booking
import kotlinx.coroutines.launch

class AdminsHomeViewModel : ViewModel() {

    private var _bookings = MutableLiveData<MutableList<AdminBooking>>()
    val bookings : LiveData<MutableList<AdminBooking>> = _bookings

    var afterParse = mutableListOf<AdminBooking>()

    fun loadBookings(adminEmail: String) {
        _bookings.value?.clear()

        Repository.getBookings(adminEmail) {
            viewModelScope.launch {
                for (book in Repository.bookngsList) {
                    afterParse.add(
                        AdminBooking(
                            book.userEmail,
                            book.professionalEmail,
                            book.hour.hour
                        )
                    )
                }

                if (afterParse.isNotEmpty()) {
                    _bookings.value = afterParse
                }
            }
        }
    }
}