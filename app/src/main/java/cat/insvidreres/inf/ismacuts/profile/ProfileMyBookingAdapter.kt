package cat.insvidreres.inf.ismacuts.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.UserPersonalBookingsProfileItemBinding
import cat.insvidreres.inf.ismacuts.recycler.RecyclerAdapter
import cat.insvidreres.inf.ismacuts.users.Booking

class ProfileMyBookingAdapter(val context: Context, var bookingsList: MutableList<Booking>)
    : RecyclerView.Adapter<ProfileMyBookingAdapter.ProfileMyBookingViewHolder>() {

        inner class ProfileMyBookingViewHolder(var binding: UserPersonalBookingsProfileItemBinding)
            : RecyclerView.ViewHolder(binding.root) {

                fun bind(booking: Booking) {
                    binding.myBookingDate.text = "${booking.days.dayOfWeek} ${booking.days.day} ${booking.hour.hour}"
                    binding.myBookingService.text = booking.product.name
                    binding.myBookingProfessional.text = booking.professionalEmail
                    binding.myBookingAmount.text = booking.product.price.toString() + " €"
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMyBookingViewHolder {
        val binding = UserPersonalBookingsProfileItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProfileMyBookingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookingsList.size
    }

    override fun onBindViewHolder(holder: ProfileMyBookingViewHolder, position: Int) {
        val booking = bookingsList[position]
        holder.bind(booking)
    }


}