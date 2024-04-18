package cat.insvidreres.inf.ismacuts.profile

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.AdminDataRvItemBinding
import java.text.DecimalFormat
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
                            val text = "Your clients think that you're a \n $formattedRatingAverage ★ \nKeep up the good work!."
                            val spannableString = SpannableString(text)

                            // Set color for the star character
                            val starColor = ContextCompat.getColor(context, R.color.yellow) // Change R.color.yellow to the desired color
                            spannableString.setSpan(ForegroundColorSpan(starColor), text.indexOf('★'), text.indexOf('★') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                            binding.adminDataQuoteTV.text = spannableString
                        }
                    } else if (item is Float) { //revenue
                        val decimalFormat = DecimalFormat("0.00")
                        val formattedRevenue = decimalFormat.format(item)
                        val euroColor = ContextCompat.getColor(context, R.color.green) // Change R.color.green to the desired color
                        val text = "You have earned a total of $formattedRevenue € \nBeautifully Done!."
                        val spannableString = SpannableString(text)
                        // Set color for the formatted revenue and euro character
                        spannableString.setSpan(ForegroundColorSpan(euroColor), text.indexOf('€'), text.indexOf('€') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                        binding.adminDataQuoteTV.text = spannableString
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