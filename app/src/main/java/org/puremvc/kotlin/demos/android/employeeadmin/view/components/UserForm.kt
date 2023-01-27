//
//  UserForm.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
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
import java.lang.Exception
import java.lang.ref.WeakReference

interface IUserForm {
    fun findById(id: Long?): User?
    fun save(user: User?, roles: List<Role>?): Long?
    fun update(user: User?, roles: List<Role>?): Int?
    fun findAllDepartments(): List<Department>?
}

class UserForm: Fragment() {

    private var user: User? = null

    private var roles: List<Role>? = null

    private val viewModel: UserViewModel by activityViewModels()

    private var _binding: UserFormBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserForm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).registerView(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserFormBinding.inflate(inflater, container, false)

        IdlingResource.increment()
        val handler = CoroutineExceptionHandler { _, e -> (activity as? EmployeeAdmin)?.alert(e)?.show() }
        lifecycleScope.launch(handler) {
            launch { // Get UI Data
                withContext(Dispatchers.IO) {
                    val items = listOf("--None Selected--") + (delegate?.findAllDepartments()?.map { it.name } ?: listOf())
                    withContext(Dispatchers.Main) { // Bind UI Data
                        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, items)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner.adapter = adapter
                    }
                }
            }

            savedInstanceState?.let { // Get User Data: State Restoration
                user = it.getParcelable("user", User::class.java)
                roles = it.getParcelableArrayList("roles", Role::class.java)
            }

            user ?: run { // Get User Data: Network
                arguments?.getLong("id")?.let {
                    launch {
                        withContext(Dispatchers.IO) {
                            user = delegate?.findById(it)
                        }
                        withContext(Dispatchers.Main) {
                            binding.username.isEnabled = false
                        }
                    }
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE
            user ?: run { user = User() } // Default User Data
            binding.user = user // Bind User Data
            user?.department?.let {
                binding.spinner.setSelection(it.id?.toInt() ?: 0)
            }

            setFragmentResultListener("roles", this::onFragmentResult) // Bind Event Handlers
            binding.apply {
                btnSave.setOnClickListener { save() }
                btnCancel.setOnClickListener { findNavController().navigate(R.id.action_userForm_to_userList) }
                btnRoles.setOnClickListener { selectRoles() }
            }

            IdlingResource.decrement()
        }

        return binding.root
    }

    private fun save() {
        binding.apply {
            user = User(arguments?.getLong("id"), username.text.toString(), first.text.toString(), last.text.toString(),
                email.text.toString(), password.text.toString(), Department(spinner.selectedItemPosition.toLong(), spinner.selectedItem.toString()))

            user?.validate(confirm.text.toString())?.let {
                (activity as? EmployeeAdmin)?.alert(Exception(it))?.show()
                return
            }
        }

        IdlingResource.increment()
        val handler = CoroutineExceptionHandler { _, e -> (activity as? EmployeeAdmin)?.alert(e)?.show() }
        lifecycleScope.launch(handler) {
            withContext(Dispatchers.IO) {
                arguments?.getLong("id")?.let {
                    delegate?.update(binding.user, roles)
                } ?: run {
                    user?.id = delegate?.save(binding.user, roles)
                }
            }
        }.invokeOnCompletion {
            user?.let { viewModel.setUser(it) }
            findNavController().navigate(R.id.action_userForm_to_userList)
            IdlingResource.decrement()
        }
    }

    private fun selectRoles() {
        val userRole = UserRole()
        arguments?.getLong("id")?.let { id ->
            userRole.arguments = bundleOf("id" to id, "roles" to roles)
        } ?: run {
            userRole.arguments = bundleOf( "roles" to roles)
        }
        userRole.show(parentFragmentManager.beginTransaction(), "dialog")
    }

    private fun onFragmentResult(requestKey: String, bundle: Bundle) {
        when (requestKey) {
            "roles" -> roles = bundle.getParcelableArrayList("roles", Role::class.java)
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putParcelable("user", binding.user)
        roles?.let { bundle.putParcelableArrayList("roles", ArrayList<Role>(it)) }
        super.onSaveInstanceState(bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setDelegate(delegate: IUserForm) {
        this.delegate = delegate
    }

}