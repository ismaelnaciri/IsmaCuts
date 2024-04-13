package cat.insvidreres.inf.ismacuts.admins.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.BookingAdminItemBinding

class AdminBookingAdapter(
    val context: Context,
    var dataset: MutableList<AdminBooking>
) : RecyclerView.Adapter<AdminBookingAdapter.AdminBookingViewHolder>() {

    inner class AdminBookingViewHolder(var binding: BookingAdminItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adminBooking: AdminBooking, position: Int) {
            binding.bookingAdminIndexTV.text = position.toString()
            binding.adminBookRVUserName.text = adminBooking.userName
            if (adminBooking.day.day != 0 && adminBooking.day.dayOfWeek != "") {
                binding.adminBookingRVTimeTV.text = adminBooking.day.dayOfWeek + " " + adminBooking.day.day + "\n" +adminBooking.hour
            }
            binding.adminBookingRVProductName.text = adminBooking.productName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminBookingAdapter.AdminBookingViewHolder {
        val binding = BookingAdminItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return AdminBookingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AdminBookingAdapter.AdminBookingViewHolder,
        position: Int
    ) {
        val adminBooking = dataset[position]
        holder.bind(adminBooking, position)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}