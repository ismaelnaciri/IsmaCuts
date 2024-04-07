package cat.insvidreres.inf.ismacuts.users.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.ServicesBinding
import cat.insvidreres.inf.ismacuts.R

class ServiceAdapter(val context: Context, var dataset: List<Service>, val itemOnClickListener: (Service) -> Unit)
    : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(var binding: ServicesBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(service: Service) {
                    var clicked = false
                    val drawableResourceId = context.resources.getIdentifier(service.src, "drawable", context.packageName)

                    if (drawableResourceId != 0) {
                        binding.itemImageToModify.setBackgroundResource(drawableResourceId)
                    } else {
                        binding.itemImageToModify.setBackgroundResource(R.drawable.barber_logo)
                    }

                    binding.root.setOnClickListener {
                        clicked = !clicked
                        if (clicked)
                            binding.mainServiceLayout.background = ContextCompat.getDrawable(context, R.drawable.booking_selected_item_corner_background)
                        else {
                            binding.mainServiceLayout.background = ContextCompat.getDrawable(context, R.drawable.main_gradient)
                        }
                    }

                    itemOnClickListener.invoke(service)
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ServicesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = dataset[position]
        holder.bind(service)
    }
}