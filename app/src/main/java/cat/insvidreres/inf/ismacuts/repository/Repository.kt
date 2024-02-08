package cat.insvidreres.inf.ismacuts.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cat.insvidreres.inf.ismacuts.model.Beardtrim
import cat.insvidreres.inf.ismacuts.model.Haircut
import cat.insvidreres.inf.ismacuts.model.User

class Repository {

    companion object {
        var haircuts: LiveData<List<Haircut>>? = null

        fun insertUser(context: Context, user: User) {

        }

        fun insertHaircut(context: Context, haircut: Haircut) {

        }

        fun insertBeardtrim(context: Context, beardtrim: Beardtrim) {

        }
    }
}