package cat.insvidreres.inf.ismacuts.recycler

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.model.User
import cat.insvidreres.inf.ismacuts.repository.Repository
import java.lang.Exception

class SelectedUserViewModel : ViewModel() {

//    private var _user = MutableLiveData<User>()
//    val user : LiveData<User> = _user

    fun updateUser(oldUser: User, newUser: User, imgUri: Uri?) {

        try {
            Repository.updateUser(oldUser, newUser, imgUri) {
                Log.i("update user function", "Updated user successfully")
            }
        } catch (e: Exception) {
            Log.e("Update User Error", e.message.toString())
        }
    }

    fun deleteUser(user: User) {
        try {
            Repository.deleteUser(user) {
                Log.i("deleteUser function", "User deleted successfully")
            }
        } catch (e: Exception) {
            Log.e("Delete User Error", e.message.toString())
        }
    }
}