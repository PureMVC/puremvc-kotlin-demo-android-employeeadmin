//
//  UserForm.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.presentation.components

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Department
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Role
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User
import org.puremvc.kotlin.demos.android.employeeadmin.presentation.MainViewModel

@AndroidEntryPoint
class UserForm: Fragment() {

    private var user: User? = null

    private var roles: List<Role>? = null

    private lateinit var viewModel: MainViewModel

    private var _binding: UserFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserFormBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, arrayListOf("--None Selected--")) // Set UI Data: Default
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }) {
            val departments = viewModel.findAllDepartments()
            adapter.addAll(departments?.map { it.name } ?: listOf()) // UI Data

            arguments?.getInt("id")?.let { // User Data
                binding.username.isEnabled = false
                user = viewModel.findById(it)
            }

            binding.progressBar.visibility = View.GONE
            binding.user = user
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            btnSave.setOnClickListener { save() }
            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            btnRoles.setOnClickListener {
                it.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({ it.isEnabled = true }, 3000)
                selectRoles()
            }
        }
        setFragmentResultListener("roles", this::onFragmentResult)
    }

    private fun save() {
        user = User(arguments?.getInt("id") ?: 0, binding.username.text.toString(), binding.first.text.toString(),
            binding.last.text.toString(), binding.email.text.toString(), binding.password.text.toString(),
            Department(binding.spinner.selectedItemPosition, binding.spinner.selectedItem.toString()))

        user?.validate(binding.confirm.text.toString())?.let {
            if (!it.first) {
                Toast.makeText(context, it.second, Toast.LENGTH_SHORT).show()
                return
            }
        }

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }) {
            user?.let { user ->
                arguments?.getInt("id")?.let {
                    viewModel.update(user, roles)
                } ?: run {
                    viewModel.save(user, roles)
                }
            }
        }.invokeOnCompletion { throwable ->
            if (throwable != null) return@invokeOnCompletion
            findNavController().popBackStack()
        }
    }

    private fun selectRoles() {
        val userRole = UserRole() // Get Data: View Initialization
        arguments?.getInt("id")?.let { id ->
            userRole.arguments = bundleOf("id" to id, "roles" to roles)
        } ?: run {
            userRole.arguments = bundleOf( "roles" to roles)
        }
        userRole.show(parentFragmentManager, "dialog") // Get Data: View
    }

    private fun onFragmentResult(requestKey: String, bundle: Bundle) { // Set Data: View
        when (requestKey) {
            "roles" -> roles = bundle.getParcelableArrayList("roles", Role::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}