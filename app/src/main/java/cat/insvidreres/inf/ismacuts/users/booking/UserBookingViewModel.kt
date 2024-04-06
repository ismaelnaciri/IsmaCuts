package cat.insvidreres.inf.ismacuts.users.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.repository.Repository

class UserBookingViewModel : ViewModel() {

    private var _days = MutableLiveData<MutableList<Days>>()
    val days : LiveData<MutableList<Days>> = _days

    private var _hours = MutableLiveData<MutableList<Hour>>()
    val hours : LiveData<MutableList<Hour>> = _hours

    fun loadDays() {
        _days.value = mutableListOf<Days>()

        Repository.getDays {
            _days.value = Repository.daysList
        }
    }

    fun loadHours() {
        _hours.value = mutableListOf<Hour>()

        Repository.getHours {
            _hours.value = Repository.hoursList
        }
    }
}