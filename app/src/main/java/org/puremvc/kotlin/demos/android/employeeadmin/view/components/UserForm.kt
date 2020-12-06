//
//  UserForm.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.user_form.*
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.Exception
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

interface IUserForm {
    fun findById(id: Long?): User?
    fun save(user: User?, roles: List<Role>?): Long?
    fun update(user: User?, roles: List<Role>?): Int?
    fun findAllDepartments(): List<Department>?
}

class UserForm: Fragment() {

    private var roles: List<Role>? = null

    private lateinit var navController: NavController

    private var delegate: IUserForm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as Application).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = UserFormBinding.inflate(inflater, container, false).apply {
            btnSave.setOnClickListener {
                if (username.isEnabled) save() else update()
            }
            btnCancel.setOnClickListener { cancel() }
            btnRoles.setOnClickListener { selectRoles() }
        }

        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, ArrayList(hashMapOf(-1L to "--None Selected--").values))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        var user: User? = null
        IdlingResource.increment()
        lifecycleScope.launch(handler) {

            launch { // UI Data
                var departments: List<Department>?
                withContext(Dispatchers.IO) {
                    departments = delegate?.findAllDepartments()

                    withContext(Dispatchers.Main) {
                        adapter.addAll(departments?.map { it.name } ?: arrayListOf())
                    }
                }
            }

            arguments?.getLong("id")?.let {
                launch { // User Data
                    withContext(Dispatchers.IO) {
                        user = delegate?.findById(it)
                    }
                }
            }

        }.invokeOnCompletion { // Stitch UI and User Data
            user?.department?.let {
                binding.spinner.setSelection(it.id?.toInt() ?: 0)
            }
            binding.user = user
            IdlingResource.decrement()
        }

        return binding.root
    }

    private fun save() {
        val user = User(arguments?.getLong("id"), username.text.toString(), first.text.toString(), last.text.toString(),
                email.text.toString(), password.text.toString(), Department(spinner.selectedItemPosition.toLong(), spinner.selectedItem.toString()))

        user.validate(confirm.text.toString())?.let {
            fault(null, Exception(it))
            return
        }

        IdlingResource.increment()
        lifecycleScope.launch(handler) {
            var id: Long? = null
            withContext(Dispatchers.IO) {
                id = delegate?.save(user, roles)

                withContext(Dispatchers.Main) {
                    if(username.isEnabled) user.id = id
                    navController.previousBackStackEntry?.savedStateHandle?.set("user", user)
                    navController.popBackStack()
                }
            }
        }.invokeOnCompletion {
            IdlingResource.decrement()
        }
    }

    private fun update() {
        val user = User(arguments?.getLong("id"), username.text.toString(), first.text.toString(), last.text.toString(),
                email.text.toString(), password.text.toString(), Department(spinner.selectedItemPosition.toLong(), spinner.selectedItem.toString()))

        user.validate(confirm.text.toString())?.let {
            fault(null, Exception(it))
            return
        }

        lifecycleScope.launch(handler) {
            withContext(Dispatchers.IO) {
                delegate?.update(user, roles)

                withContext(Dispatchers.Main) {
                    navController.previousBackStackEntry?.savedStateHandle?.set("user", user)
                    navController.popBackStack()
                }
            }
        }.invokeOnCompletion {
            IdlingResource.decrement()
        }
    }

    private fun cancel() {
        navController.navigate(R.id.action_userForm_to_userList)
    }

    private fun selectRoles() {
        val userRole = UserRole()
        arguments?.getLong("id")?.let { id ->
            userRole.arguments = bundleOf("id" to (id), "roles" to roles)
        } ?: run {
            userRole.arguments = bundleOf( "roles" to roles)
        }
        userRole.setTargetFragment(this@UserForm, 1)
        userRole.show(parentFragmentManager.beginTransaction(), "dialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val obj = data?.extras?.getSerializable("roles")
            if (obj is List<*>) {
                roles = ArrayList(obj.filterIsInstance<Role>())
            }
        }
    }

    private fun fault(context: CoroutineContext?, exception: Throwable) {
        Log.d("UserForm", "Error: ${exception.localizedMessage} $context")
        (activity as? EmployeeAdmin)?.fault(exception)
    }

    private val handler = CoroutineExceptionHandler { context, exception -> fault(context, exception) }

    fun setDelegate(delegate: IUserForm) {
        this.delegate = delegate
    }

}