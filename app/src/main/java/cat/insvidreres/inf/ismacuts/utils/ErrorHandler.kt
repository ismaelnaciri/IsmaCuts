package cat.insvidreres.inf.ismacuts.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog

interface ErrorHandler {
    fun showAlert(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("S'ha produït un error autenticant l'usuario")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun logError(tag: String, message: String) {
        Log.d(tag, message)
    }

}