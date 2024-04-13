package cat.insvidreres.inf.ismacuts.login

import androidx.lifecycle.ViewModel
import cat.insvidreres.inf.ismacuts.repository.Repository

class LoginViewModel: ViewModel() {


    fun loginWithEmailAndPw(email: String, password: String, onComplete: (String) -> Unit) {
        Repository.signIn(email, password) {
            onComplete(it)
        }
    }
}