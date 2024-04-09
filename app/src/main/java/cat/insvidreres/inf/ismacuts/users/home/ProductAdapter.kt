package cat.insvidreres.inf.ismacuts.users.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.ProductBinding
import cat.insvidreres.inf.ismacuts.users.booking.UsersBookingFragment
import com.bumptech.glide.Glide

class ProductAdapter(
    val context: Context,
    var dataset: List<Product>,
    val itemOnClickListener: (Product) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(var binding: ProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            Glide.with(binding.productImageView.context).load(product.img)
                .into(binding.productImageView)
            binding.selectProductButton.setOnClickListener {
                println(product.name + " selected")
                itemOnClickListener(product)
            }
        }
    }

    interface NavigationListener {
        fun navigateToBookingFragment()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = dataset[position]
        holder.bind(product)
    }
}