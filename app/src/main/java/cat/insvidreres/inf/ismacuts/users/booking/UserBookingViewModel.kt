package cat.insvidreres.inf.ismacuts.users.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.repository.Repository
import cat.insvidreres.inf.ismacuts.utils.ServicesType

class UserBookingViewModel : ViewModel() {

    private var _days = MutableLiveData<MutableList<Days>>()
    val days : LiveData<MutableList<Days>> = _days

    private var _hours = MutableLiveData<MutableList<Hour>>()
    val hours : LiveData<MutableList<Hour>> = _hours

    private var _professionals = MutableLiveData<List<Professional>>()
    val professionals : LiveData<List<Professional>> = _professionals

    var selectedOptions: MutableList<Any> = mutableListOf()

    fun updateSelectedItems(item: Any, onError: () -> Unit, onDelete: () -> Unit) {
        if (selectedOptions.size <= 3) {
            if (item is Professional) {
                if (selectedOptions.contains(item::class)) {
                    onDelete()
                    selectedOptions.remove(item::class)
                    println("selectedOptions by deleted: $selectedOptions")
                } else {
                    selectedOptions.add(item)
                    println("selectedOptions by added: $selectedOptions")
                }
            } else if (item is Hour && selectedOptions.contains(Professional::class)) {
                if (selectedOptions.contains(item::class)) {
                    onDelete()
                    selectedOptions.remove(item::class)
                    println("selectedOptions by deleted: $selectedOptions")
                } else {
                    selectedOptions.add(item)
                    println("selectedOptions by added: $selectedOptions")
                }
            } else if (item is Days && selectedOptions.contains(Professional::class)) {
                if (selectedOptions.contains(item.javaClass)) {
                    onDelete()
                    selectedOptions.remove(item.javaClass)
                    println("selectedOptions by deleted: $selectedOptions")
                } else {
                    selectedOptions.add(item)
                    println("selectedOptions by added: $selectedOptions")
                }
            } else {
                onError()
            }
        }
    }

    fun loadDays() {
        _days.value = mutableListOf<Days>()

        Repository.getDays {
            _days.value = Repository.daysList
        }
    }

    fun loadHours(name: String) {
        _hours.value = mutableListOf<Hour>()

        Repository.getHours(name) {
            _hours.value = Repository.hoursList
        }
    }

    fun loadProfessionals() {
        _professionals.value = mutableListOf<Professional>()

        Repository.getProfesionals(ServicesType.HAIRCUT.toString()) {
            _professionals.value = Repository.professionalList
        }
    }

    fun removeHourFromProfessional(name: String, valueToDelete: String) {
        Repository.updateProfessionalAvailableHours(name, valueToDelete) {
            _hours.value = Repository.hoursList
        }
    }
}