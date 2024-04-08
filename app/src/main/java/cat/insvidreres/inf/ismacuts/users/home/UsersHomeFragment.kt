package cat.insvidreres.inf.ismacuts.users.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersHomeBinding


class UsersHomeFragment : Fragment() {

    private lateinit var binding: FragmentUsersHomeBinding
    private val viewModel: UsersHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersHomeBinding.inflate(inflater)
        val servicesRecyclerView = binding.horizontalServicesRV
        val productsRecyclerView = binding.usersHomeProductsRV

        servicesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        productsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.resetSelectedOptions()
        viewModel.loadServices()
        viewModel.loadProducts("")

        val productsAdapter = ProductAdapter(requireContext(), emptyList()) { selectedProduct ->
            //TODO Save service selected into a sharedviewmodel
        }

        val serviceAdapter = ServiceAdapter(requireContext(), emptyList()) { selectedService ->
            Toast.makeText(
                requireContext(),
                "Service Selected: " + selectedService.name,
                Toast.LENGTH_LONG
            ).show()

            viewModel.updateSelectedItems(selectedService,
                onError = {
                    Toast.makeText(
                        requireContext(),
                        "Error wtf",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDelete = {
                    Toast.makeText(
                        requireContext(),
                        "Deleted item: $selectedService",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("Deleted item: $selectedService")
                })

            viewModel.loadProducts(selectedService.serviceType)
            productsAdapter.notifyDataSetChanged()
        }


        viewModel.services.observe(viewLifecycleOwner) { servicesList ->
            serviceAdapter.dataset = servicesList
            binding.horizontalServicesRV.adapter = serviceAdapter
        }

        viewModel.products.observe(viewLifecycleOwner) { productsList ->
            productsAdapter.dataset = productsList
            binding.usersHomeProductsRV.adapter = productsAdapter
        }

        return binding.root
    }


}