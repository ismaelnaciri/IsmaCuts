package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.HoursBinding

class HourAdapter(val context: Context, var dataset: List<Hour>, val itemClickListener: (Hour) -> Unit) :
    RecyclerView.Adapter<HourAdapter.HourViewHolder>() {

        //Do GridLayout
        //<androidx.recyclerview.widget.RecyclerView
        //    android:id="@+id/recyclerView"
        //    android:layout_width="match_parent"
        //    android:layout_height="match_parent"
        //    android:clipToPadding="false"
        //    android:padding="8dp"
        //    app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
        //    app:spanCount="3" <!-- Set the number of items per row -->
        //    app:orientation="vertical"
        //    tools:listitem="@layout/item_layout" />
        inner class HourViewHolder(var binding: HoursBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(hour: Hour) {
                    binding.firstHourButton.text = hour.hour
                    binding.firstHourButton.setOnClickListener {
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