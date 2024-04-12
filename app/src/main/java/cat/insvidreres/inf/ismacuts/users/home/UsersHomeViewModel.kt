package cat.insvidreres.inf.ismacuts.users.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.launch

class UsersHomeViewModel : ViewModel() {

    private var _services = MutableLiveData<MutableList<Service>>()
    val services: LiveData<MutableList<Service>> = _services

    private var _products = MutableLiveData<MutableList<Product>>()
    val products: LiveData<MutableList<Product>> = _products

    var selectedOptions: MutableList<Any> = mutableListOf()

    fun updateSelectedItems(item: Any, onError: () -> Unit, onDelete: () -> Unit) {
        if (selectedOptions.size <= 2) {
            // Check if the item is a Service or Product
            if (item is Service || item is Product) {
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

    fun resetSelectedOptions() {
        this.selectedOptions.clear()
    }

    fun loadServices() {
        _services.value?.clear()

        Repository.getServices {
            viewModelScope.launch {
                _services.value = Repository.servicesList
            }
        }
    }

    fun loadProducts(serviceType: String) {
        _products.value?.clear()

        Repository.getProducts(serviceType) {
            viewModelScope.launch {
                _products.value = Repository.productsList
                println("very sussy indeed")
            }
        }
    }

}