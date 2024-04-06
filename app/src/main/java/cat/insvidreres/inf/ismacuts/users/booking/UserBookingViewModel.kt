package cat.insvidreres.inf.ismacuts.users.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserBookingViewModel : ViewModel() {

    private var _days = MutableLiveData<MutableList<Days>>()
    val days : LiveData<MutableList<Days>> = _days

    private var _hours = MutableLiveData<MutableList<Hour>>()
    val hours : LiveData<MutableList<Hour>> = _hours

    fun loadDays() {
        _days.value = mutableListOf<Days>()

        //TODO Make repository call
    }

    fun loadHours() {
        _hours.value = mutableListOf<Hour>()

        //TODO Make repository call

    }
}