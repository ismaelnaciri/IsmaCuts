package cat.insvidreres.inf.ismacuts.admins.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.launch

class AdminsHomeViewModel : ViewModel() {

    private var _bookings = MutableLiveData<MutableList<AdminBooking>>()
    val bookings: LiveData<MutableList<AdminBooking>> = _bookings

    fun loadBookings(adminEmail: String) {
        _bookings.value?.clear()
        _bookings.value = mutableListOf<AdminBooking>()

        Repository.getBookings(adminEmail, true) {
            viewModelScope.launch {
                Repository.adminBookingsList.sortBy { it.day.day.toInt() }
                println("adminsBookingList from viewModel | ${Repository.adminBookingsList}")
                _bookings.value = Repository.adminBookingsList
            }
        }
    }

    fun confirmBooking(professionalEmail: String, booking: AdminBooking, onComplete: () -> Unit) {
        viewModelScope.launch {
            Repository.confirmBooking(professionalEmail, booking, onComplete = {
                println("hope it works, confirm")

            },
                onError = {
                    println("ERROR | $it")
                })

            onComplete()
        }
    }

    fun deleteBooking(professionalEmail: String, booking: AdminBooking, onComplete: () -> Unit) {
        viewModelScope.launch {
            Repository.deleteBooking(professionalEmail, booking) {
                println("hope it works, delete")
            }

            onComplete()
        }
    }
}