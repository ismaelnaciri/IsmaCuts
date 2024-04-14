package cat.insvidreres.inf.ismacuts.admins.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.launch

class AdminsDashboardViewModel : ViewModel() {

    private var _revenueData = MutableLiveData<MutableList<RevenueData>>()
    val revenueData: LiveData<MutableList<RevenueData>> = _revenueData

    private var _professionalName = MutableLiveData<String>()
    val professionalName: LiveData<String> = _professionalName

    fun loadCurrentMonthRevenueData(
        professionalEmail: String,
        currentMonth: Int,
        onMonthOutOfBounds: () -> Unit
    ) {
        _revenueData.value?.clear()
        _revenueData.value = mutableListOf<RevenueData>()

        Repository.getRevenueFromProfessional(professionalEmail, currentMonth, onComplete = {
            viewModelScope.launch {
                _revenueData.value = Repository.revenueList
            }
        },
            onMonthOutOfBounds = {
                println("out of bounds caught in viewModel")
                onMonthOutOfBounds()
            });
    }

    fun loadProfessionalName(professionalEmail: String) {
        viewModelScope.launch {
            Repository.getDetailsWithEmail(professionalEmail, true) {
                _professionalName.value = Repository.professionalList[0].name
            }
        }
    }

    fun resetRevenueDataList() {
        this._revenueData.value?.clear()
        this._revenueData.value = mutableListOf<RevenueData>()
    }
}