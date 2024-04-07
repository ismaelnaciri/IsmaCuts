package cat.insvidreres.inf.ismacuts.users.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.ProductBinding

class ProductAdapter(val context: Context, var dataset: List<Product>, val itemOnClickListener: (Product) -> Unit)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        inner class ProductViewHolder(var binding: ProductBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(product: Product) {
                    //TODO put the url into the image
                }
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