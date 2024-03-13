package cat.insvidreres.inf.ismacuts.recycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.insvidreres.inf.ismacuts.databinding.UserItemEntregaBinding
import cat.insvidreres.inf.ismacuts.model.User
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class RecyclerAdapter(val context: Context, var usersList: List<User>, private var binding: UserItemEntregaBinding, val itemOnClickListener: (User) -> Unit):
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(var binding: UserItemEntregaBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.recyclerUsernameTV.text = user.username
            binding.recyclerEmailTV.text = user.email
            binding.recyclerAdminAnswerTV.text = user.admin.toString()

//            Glide.with(binding.userRecyclerIV.context).load(user.img).into(binding.userRecyclerIV)
//            Picasso.get().load(user.img).into(binding.userRecyclerIV)
            Log.d("Img url from bind fun", user.img)
            binding.root.setOnClickListener {
                itemOnClickListener.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = UserItemEntregaBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val user = usersList[position]
        holder.bind(user)
//        with(holder.binding) {
//            with(usersList[position]) {
//                Picasso.get().load(this.img).into(binding.userRecyclerIV)
//                holder.bind(user)
//            }
//        }
    }
}