package cat.insvidreres.inf.ismacuts.users.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentUsersHomeBinding
import cat.insvidreres.inf.ismacuts.users.HomeBookingSharedViewModel
import cat.insvidreres.inf.ismacuts.users.booking.UsersBookingFragment


class UsersHomeFragment : Fragment() {

    private lateinit var binding: FragmentUsersHomeBinding
    private val viewModel: UsersHomeViewModel by viewModels()
    private val bookingSharedViewModel: HomeBookingSharedViewModel by activityViewModels()

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

        try {
            for (item in bookingSharedViewModel.selectedOptions) {
                if (item is Product)
                    bookingSharedViewModel.selectedOptions.remove(item)
            }
        } catch (e: Exception) {
            Log.e("For loop home fragment", "Error | ${e.message}")
        }

        viewModel.resetSelectedOptions()
        viewModel.loadServices()
        viewModel.loadProducts("")

        val productsAdapter = ProductAdapter(
            requireContext(), emptyList(),
            itemOnClickListener = { selectedProduct ->
                println("Product Selected: ${selectedProduct.name}")

                bookingSharedViewModel.updateSelectedItems(selectedProduct,
                    onError = {
                        print("product fuck gg item")
                    },
                    onDelete = {
                        print("${selectedProduct.name} deleted from updateSelectedItems")
                    })
                findNavController().navigate(R.id.usersBookingFragment)
            },
        )


        val serviceAdapter = ServiceAdapter(requireContext(), emptyList()) { selectedService ->
            Toast.makeText(
                requireContext(),
                "Service Selected: " + selectedService.name,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.updateSelectedItems(selectedService,
                onError = {
                    print("Error wtf")
                },
                onDelete = {
                    println("Deleted item: $selectedService")
                })

            if (viewModel.products.value?.any { it.serviceType != selectedService.serviceType }!!) {
                println("if works?  | ${viewModel.products.value?.any { it.serviceType != selectedService.serviceType }}")
                println("Let's see this shit  | ${viewModel.products.value}")
                viewModel.loadProducts(selectedService.serviceType)
            } else {
                viewModel.loadProducts("")
            }
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