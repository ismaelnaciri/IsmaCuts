package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.UserItemEntregaBinding
import cat.insvidreres.inf.ismacuts.model.Professional
import cat.insvidreres.inf.ismacuts.model.User

class ProfessionalAdapter(val context: Context, var dataset: List<User>, val itemOnClickListener: (User) -> Unit) :
    RecyclerView.Adapter<ProfessionalAdapter.ProfessionalViewHolder>() {

        //Change to professional rv item binging
        inner class ProfessionalViewHolder(var binding: UserItemEntregaBinding)
            : RecyclerView.ViewHolder(binding.root) {

                fun bind(professional: Professional) {
                    var clicked = false

                }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessionalViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ProfessionalViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}