package cat.insvidreres.inf.ismacuts.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.AdminDataRvItemBinding
import java.util.Locale

class AdminDataRecyclerAdapter(val context: Context, var dataset: MutableList<Any>)
    : RecyclerView.Adapter<AdminDataRecyclerAdapter.AdminDataRecyclerViewHolder>() {

        inner class AdminDataRecyclerViewHolder(var binding: AdminDataRvItemBinding)
            : RecyclerView.ViewHolder(binding.root) {

                fun bind(item: Any) {
                    //Reviews
                    if (item is MutableList<*>) {
                        if (item.isNotEmpty() && item[0] is Number) {
                            val ratingAverage = item.map { (it as Number).toFloat() }.average()
                            val formattedRatingAverage = String.format(Locale.getDefault(), "%.2f", ratingAverage)
                            binding.adminDataQuoteTV.text = "Your clients think that you're a \n $formattedRatingAverage ★ \nKeep up the good work!."
                        }
                    } else if (item is Float) { //revenue
                        val formattedRevenueAverage = String.format(Locale.getDefault(), "%.2f", item)
                        binding.adminDataQuoteTV.text = "You have earned a total of ${formattedRevenueAverage} €. \nBeautifully Done!."
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDataRecyclerViewHolder {
        val binding = AdminDataRvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return AdminDataRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: AdminDataRecyclerViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }
}