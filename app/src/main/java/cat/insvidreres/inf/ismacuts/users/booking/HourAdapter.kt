package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.HoursBinding

class HourAdapter(val context: Context, var dataset: List<Hour>, val itemClickListener: (Hour) -> Unit) :
    RecyclerView.Adapter<HourAdapter.HourViewHolder>() {

        inner class HourViewHolder(var binding: HoursBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(hour: Hour) {
                    var clicked = false
                    binding.firstHourButton.text = hour.hour
                    binding.firstHourButton.setOnClickListener {
                        clicked = !clicked
                        if (clicked)
                            binding.firstHourButton.background = ContextCompat.getDrawable(context, R.drawable.booking_selected_item_corner_background)
                        else
                            binding.firstHourButton.background = ContextCompat.getDrawable(context, R.drawable.hour_rounded_corner_background)

                        itemClickListener.invoke(hour)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = HoursBinding.inflate(LayoutInflater.from(context), parent, false)
        return HourViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val day = dataset[position]
        holder.bind(day)
    }
}