//
//  UserForm.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserForm {
    suspend fun findById(id: Long): Map<User, Department>?
    suspend fun save(user: User, roles: List<Role>?): Long?
    suspend fun update(user: User, roles: List<Role>?): Int?
    suspend fun findAllDepartments(): List<Department>?
}

class UserForm: Fragment() {

    private var map: Map<User, Department>? = null

    private var user: User? = null

    private var roles: List<Role>? = null

    private var _binding: UserFormBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserForm? = null

    companion object {
        const val TAG = "UserForm"
    }

    init {
        ApplicationFacade.getInstance(ApplicationFacade.KEY).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserFormBinding.inflate(inflater, container, false)

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e)?.show()
        }) {
            IdlingResource.increment()

            launch { // Get UI Data: IO
                val items = listOf("--None Selected--") + (delegate?.findAllDepartments()?.map { it.name } ?: listOf())
                val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, items)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }

            launch { // Get User Data: IO
                arguments?.getLong("id")?.let {
                    binding.username.isEnabled = false
                    map = delegate?.findById(it)
                    user = map?.keys?.first()
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE
            binding.user = user // Set User Data
            val department = map?.values?.first()
            department?.let { binding.spinner.setSelection(it.id.toInt()) }
            IdlingResource.decrement()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            btnSave.setOnClickListener { save() }
            btnCancel.setOnClickListener {
                findNavController().navigate(R.id.action_userForm_to_userList)
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
        user = User(arguments?.getLong("id") ?: 0, binding.username.text.toString(), binding.first.text.toString(), binding.last.text.toString(),
            binding.email.text.toString(), binding.password.text.toString(), binding.spinner.selectedItemPosition.toLong())

        user?.validate(binding.confirm.text.toString())?.let {
            (activity as? EmployeeAdmin)?.alert(java.lang.Exception(it))?.show()
            return
        }

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e)?.show()
        }) {
            IdlingResource.increment()
            arguments?.getLong("id")?.let {
                delegate?.update(user!!, roles)
            } ?: run {
                user?.id = delegate?.save(user!!, roles) ?: 0
            }
        }.invokeOnCompletion {
            IdlingResource.decrement()
            findNavController().navigate(R.id.action_userForm_to_userList)
        }
    }

    private fun selectRoles() {
        val userRole = UserRole() // Get Data: View Initialization
        arguments?.getLong("id")?.let { id ->
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

    fun setDelegate(delegate: IUserForm) {
        this.delegate = delegate
    }

}