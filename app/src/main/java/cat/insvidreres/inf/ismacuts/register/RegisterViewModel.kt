package cat.insvidreres.inf.ismacuts.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.repository.Repository

class RegisterViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    fun createAccount(user: User) {
        Repository.insertUser(user)
    }
}