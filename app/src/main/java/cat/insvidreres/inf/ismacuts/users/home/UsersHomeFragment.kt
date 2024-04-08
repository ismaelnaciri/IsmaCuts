package cat.insvidreres.inf.ismacuts.users.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersHomeBinding
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel


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

        val bookingSVM = ViewModelProvider(requireActivity())[HomeBookingSharedViewModel::class.java]

        servicesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        productsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.resetSelectedOptions()
        viewModel.loadServices()
        viewModel.loadProducts("")

        val productsAdapter = ProductAdapter(requireContext(), emptyList()) { selectedProduct ->
            Toast.makeText(
                requireContext(),
                "Product Selected: " + selectedProduct.name,
                Toast.LENGTH_LONG
            ).show()

            bookingSVM.updateSelectedItems(selectedProduct.name,
                onError = {
                    print("product fuck gg item")
                },
                onDelete = {
                    print("${selectedProduct.name} deleted from updateSelectedItems")
                })
        }

        val serviceAdapter = ServiceAdapter(requireContext(), emptyList()) { selectedService ->
            Toast.makeText(
                requireContext(),
                "Service Selected: " + selectedService.name,
                Toast.LENGTH_LONG
            ).show()

            viewModel.updateSelectedItems(selectedService,
                onError = {
                    print("Error wtf")
                },
                onDelete = {
                    println("Deleted item: $selectedService")
                })

            viewModel.loadProducts(selectedService.serviceType)
            productsAdapter.notifyDataSetChanged()
        }


        viewModel.services.observe(viewLifecycleOwner) { servicesList ->
            serviceAdapter.dataset = servicesList
            binding.horizontalServicesRV.adapter = serviceAdapter
            serviceAdapter.notifyDataSetChanged()
        }

        viewModel.products.observe(viewLifecycleOwner) { productsList ->
            productsAdapter.dataset = productsList
            binding.usersHomeProductsRV.adapter = productsAdapter
            productsAdapter.notifyDataSetChanged()
        }

        return binding.root
    }


}