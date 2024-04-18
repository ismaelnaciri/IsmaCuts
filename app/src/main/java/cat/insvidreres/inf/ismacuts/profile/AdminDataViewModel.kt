package cat.insvidreres.inf.ismacuts.profile

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.admins.dashboard.RevenueData
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AdminDataViewModel : ViewModel() {

    private var _professional = MutableLiveData<Professional>()
    val professional: LiveData<Professional> = _professional

    private var _professionalPropertiesList = MutableLiveData<MutableList<Any>>()
    val professionalPropertiesList: LiveData<MutableList<Any>> = _professionalPropertiesList

    private var _revenueData = MutableLiveData<MutableList<RevenueData>>()

    var totalRevenue: Float = 0.0f

    fun loadProfessional(professionalEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.getDetailsWithEmail(professionalEmail, true) {
                _professional.value = Repository.professionalList[0]
                println("_professional value: | ${_professional.value}")
                _professionalPropertiesList.value = mutableListOf<Any>()
                _professionalPropertiesList.value?.add(Repository.professionalList[0].reviews)
                _professionalPropertiesList.value?.add(totalRevenue)
                println("_professionalProperties value: | ${_professionalPropertiesList.value}")
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

    fun loadTotalRevenue() {
        totalRevenue = 0.0f
        _revenueData.value?.forEach { item ->
            totalRevenue += item.revenue.toFloat()
        }
        println("TOTAL REVENUE? | $totalRevenue")
    }

    fun loadMonth(currentMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, currentMonth)
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}