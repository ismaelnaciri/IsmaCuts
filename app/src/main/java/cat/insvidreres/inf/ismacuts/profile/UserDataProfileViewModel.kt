package cat.insvidreres.inf.ismacuts.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.repository.Repository
import cat.insvidreres.inf.ismacuts.users.Booking
import kotlinx.coroutines.launch

class UserDataProfileViewModel : ViewModel() {

    private var _professionalName = MutableLiveData<String>()
    val professionalName: LiveData<String> = _professionalName

    private var _bookings = MutableLiveData<MutableList<Booking>>()
    val bookings : LiveData<MutableList<Booking>> = _bookings

    fun loadProfessionalName(professionalEmail: String) {
        viewModelScope.launch {
            Repository.getDetailsWithEmail(professionalEmail, true) {
                _professionalName.value = Repository.professionalList[0].name
            }
        }
    }

    fun loadBookings(userEmail: String) {
        viewModelScope.launch {
            Repository.getBookings(userEmail) {
                Repository.bookingsList.sortBy { it.days.day.toInt() }
                _bookings.value = Repository.bookingsList
            }
        }
    }
}