package cat.insvidreres.inf.ismacuts.admins.home

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.admins.dashboard.RevenueData
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AdminsHomeViewModel : ViewModel() {

    private var _bookings = MutableLiveData<MutableList<AdminBooking>>()
    val bookings: LiveData<MutableList<AdminBooking>> = _bookings

    private var _professionalName = MutableLiveData<String>()
    val professionalName: LiveData<String> = _professionalName

    private var _revenueData = MutableLiveData<MutableList<RevenueData>>()
    private var _todayRevenue = MutableLiveData<Float>()
    val todayRevenue: LiveData<Float> = _todayRevenue

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

    fun loadProfessionalName(professionalEmail: String) {
        viewModelScope.launch {
            Repository.getDetailsWithEmail(professionalEmail, true) {
                _professionalName.value = Repository.professionalList[0].name
            }
        }
    }

    fun loadCurrentMonthRevenueData(
        professionalEmail: String,
        currentMonth: Int,
        onMonthOutOfBounds: () -> Unit,
        onComplete: () -> Unit
    ) {
        _revenueData.value?.clear()
        _revenueData.value = mutableListOf<RevenueData>()

        viewModelScope.launch {
            Repository.getRevenueFromProfessional(professionalEmail, currentMonth, onComplete = {
                _revenueData.value = Repository.revenueList
                onComplete()
            },
                onMonthOutOfBounds = {
                    println("out of bounds caught in viewModel")
                    onMonthOutOfBounds()
                })
        }
    }

    fun loadTodayRevenue(dayNumber: Number) {
        _todayRevenue.value = 0.00f
        _revenueData.value?.forEach { item ->
            if (item.dayNumber == dayNumber) {
                _todayRevenue.value = _todayRevenue.value!! + item.revenue.toFloat()
            }
        }
        println("TODAY REVENUE? | $todayRevenue")
    }

    fun loadMonth(currentMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, currentMonth)
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}