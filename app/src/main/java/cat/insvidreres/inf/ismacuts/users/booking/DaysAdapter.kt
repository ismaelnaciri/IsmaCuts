package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import androidx.core.content.ContextCompat
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.DaysBinding

class DaysAdapter(val context: Context, var dataset: List<Days>, val itemOnClickListener: (Days) -> Unit) :
    RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    inner class DaysViewHolder(var binding: DaysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Days) {
            var clicked = false
            binding.dayTV.text = day.dayOfWeek.take(3)
            binding.dayNumber.text = day.day.toString()
            val context: Context = binding.root.context
            binding.root.setOnClickListener {
                clicked = !clicked
                if (clicked)
                    binding.dayContainer.background = ContextCompat.getDrawable(context, R.drawable.booking_selected_item_corner_background)
                else
                    binding.dayContainer.background = ContextCompat.getDrawable(context, R.drawable.day_rounded_corner_background)

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