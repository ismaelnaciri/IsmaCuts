package cat.insvidreres.inf.ismacuts.users.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.model.Professional
import com.bumptech.glide.Glide
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.ProfessionalBinding

class ProfessionalAdapter(
    val context: Context,
    var dataset: List<Professional>,
    val itemOnClickListener: (Professional) -> Unit
) :
    RecyclerView.Adapter<ProfessionalAdapter.ProfessionalViewHolder>() {

    inner class ProfessionalViewHolder(var binding: ProfessionalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(professional: Professional) {
            var clicked = false
            binding.professionalNameTV.text = professional.name

            //TODO ratingbar rating is average of all reviews from professional
            var ratingAverage = 0.0
            for (rating in professional.reviews) {
                ratingAverage += rating.toFloat()
            }
            ratingAverage /= professional.reviews.size
            binding.professionalRatingBar.rating = ratingAverage.toFloat()

            Glide.with(binding.professionalImageView.context).load(professional.img).into(binding.professionalImageView)

            binding.root.setOnClickListener {
                clicked = !clicked
                if (clicked)
                    binding.proffessionalBackgroudLayout.background = ContextCompat.getDrawable(context, R.drawable.booking_selected_item_corner_background)
                else
                    binding.proffessionalBackgroudLayout.background = ContextCompat.getDrawable(context, R.drawable.professional_rounded_background)

                itemOnClickListener.invoke(professional)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessionalViewHolder {
        val binding = ProfessionalBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProfessionalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ProfessionalViewHolder, position: Int) {
        val professional = dataset[position]
        holder.bind(professional)
    }
}