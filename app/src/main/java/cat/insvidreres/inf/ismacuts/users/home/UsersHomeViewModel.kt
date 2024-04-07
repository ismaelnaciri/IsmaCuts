package cat.insvidreres.inf.ismacuts.users.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.repository.Repository

class UsersHomeViewModel : ViewModel() {

    private var _services = MutableLiveData<MutableList<Service>>()
    val services : LiveData<MutableList<Service>> = _services



    var selectedOptions: MutableList<Any> = mutableListOf()

    fun updateSelectedItems(item: Any, onError: () -> Unit, onDelete: () -> Unit) {
        if (selectedOptions.size <= 2) {
            // Check if the item is a Professional or Hour or Days
            //|| item is Product
            if (item is Service) {
                val itemClass = item::class.java
                val isItemAlreadySelected = selectedOptions.any { it.javaClass == itemClass }

                if (isItemAlreadySelected) {
                    onDelete()
                    selectedOptions.removeAll { it.javaClass == itemClass }
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

    fun loadServices() {
        _services.value = mutableListOf()

        Repository.getServices {
            _services.value = Repository.servicesList
        }
    }

}