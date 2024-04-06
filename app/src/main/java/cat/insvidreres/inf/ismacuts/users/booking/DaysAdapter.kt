package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.DaysBinding

class DaysAdapter(val context: Context, val dataset: List<Days>, val itemOnClickListener: (Days) -> Unit) :
    RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    inner class DaysViewHolder(var binding: DaysBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(day: Days) {
                binding.dayTV.text = day.day
                binding.dayNumber.text = day.dayOfWeek.toString()
                binding.root.setOnClickListener {
                    itemOnClickListener.invoke(day)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding = DaysBinding.inflate(LayoutInflater.from(context), parent, false)
        return DaysViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val day = dataset[position]
        holder.bind(day)
    }
}