package cat.insvidreres.inf.ismacuts.recycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.repository.Repository

class RecyclerViewModel : ViewModel() {

    private var _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>> = _users

    fun loadUsers() {
        _users.value = mutableListOf<User>()

        Repository.addUsersToList {
            _users.value = Repository.recyclerList

        }
    }
}