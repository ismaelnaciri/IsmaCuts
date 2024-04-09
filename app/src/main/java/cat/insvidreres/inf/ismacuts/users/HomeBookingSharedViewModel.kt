package cat.insvidreres.inf.ismacuts.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.repository.Repository
import cat.insvidreres.inf.ismacuts.users.booking.Days
import cat.insvidreres.inf.ismacuts.users.booking.Hour
import cat.insvidreres.inf.ismacuts.users.home.Product
import cat.insvidreres.inf.ismacuts.users.home.Service

class HomeBookingSharedViewModel : ViewModel() {

    //To get for admin
    private var _booking = MutableLiveData<MutableList<Booking>>()
    val booking: LiveData<MutableList<Booking>> = _booking

    var _book = MutableLiveData<Booking>()

    var selectedOptions: MutableList<Any> = mutableListOf()
    fun updateSelectedItems(item: Any, onError: () -> Unit, onDelete: () -> Unit) {
        if (selectedOptions.size <= 5) {
            // Check if the item is User or Product or Professional or Days or Hour
            if ((item is String && item.contains("@") && selectedOptions.count { it is String && it.contains("@") } <= 2)
                || item is Product
                || item is Days
                || item is Hour
            ) {
                if (item is String && item.contains("@")) {
                    val emailCount = selectedOptions.filterIsInstance<String>().count { it.contains("@") }
                    if (emailCount < 2) {
                        selectedOptions.add(item)
                        println("selectedOptions by added: $selectedOptions")
                    } else {
                        if(selectedOptions.any { it.toString() == item })
                            selectedOptions.remove(item)
                    }
                } else {
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
                }
            } else {
                onError()
            }
        }

        println("All selected items | $selectedOptions")
    }



    fun checkIfAnyItemNull() : Boolean {
        return (_book.value?.userEmail != null
                && _book.value?.product != null
                && _book.value?.professionalEmail != null
                && _book.value?.days != null
                && _book.value?.hour != null)
    }


    fun generateBookingInsert() {
        try {
            var product: Product? = null
            var days: Days? = null
            var hour: Hour? = null
            var userEmail: String? = null
            var professionalEmail: String? = null

            for (option in selectedOptions) {
                when (option) {
                    is Product -> product = option
                    is Days -> days = option
                    is Hour -> hour = option
                    is String -> {
                        if (option.contains("@")) {
                            if (userEmail == null) {
                                userEmail = option
                            } else if (professionalEmail == null) {
                                professionalEmail = option
                            }
                        }
                    }
                }
            }

            if (product != null && days != null && hour != null && userEmail != null && professionalEmail != null) {

                println("ok no values are null")
                _book.value = Booking(userEmail, product, professionalEmail, days, hour)
                println("sussy _book ${_book.value}")
            } else {
                print("GGGGGGGGGGG")
            }
        } catch (e: Exception) {
            print("ERROR | " + e.message)

        }
    }


    fun insertBooking() {
        _book.value?.let { book ->
            Repository.insertBooking(
                book,
                onComplete = {
                    print("Insert correct!!!! PRRRRRRRR")
                },
                onError = {
                    print("ERROR IN INSERT | $it")
                }
            )
        }
    }

    fun loadBookings(professionalName: String) {

    }
}