package cat.insvidreres.inf.ismacuts.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private var _professional = MutableLiveData<Professional>()
    val professional : LiveData<Professional> = _professional


    fun loadUser(userEmail: String) {
        viewModelScope.launch {
            Repository.getDetailsWithEmail(userEmail, false) {
                println("usersList from vm | ${Repository.usersList}")
                _user.value = Repository.usersList[0]
            }
        }
    }

    fun loadProfessional(professionalEmail: String) {
        viewModelScope.launch {
            Repository.getDetailsWithEmail(professionalEmail, true) {
                println("professionalList from vm | ${Repository.professionalList}")
                _professional.value = Repository.professionalList[0]
            }
        }
    }

    fun updateProfilePic(email: String, isProfessional: Boolean, imgUri: Uri?) {
        viewModelScope.launch {
            Repository.updateProfilePic(email, isProfessional, imgUri)
        }
    }
}