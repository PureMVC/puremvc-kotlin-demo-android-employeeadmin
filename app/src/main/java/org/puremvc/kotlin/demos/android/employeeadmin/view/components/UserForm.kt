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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import java.lang.Exception
import java.lang.ref.WeakReference

class UserForm: Fragment() {

    private var roles: HashMap<Long, String>? = null

    private lateinit var navController: NavController

    private var delegate: IUserForm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as Application).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = UserFormBinding.inflate(inflater, container, false).apply {

            val departments = hashMapOf(-1L to "--None Selected--")
            ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, ArrayList(departments.values)).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            MainScope().launch(handler) {

                var deferred1: Deferred<UserVO?>? = null
                arguments?.getLong("id")?.let {
                    deferred1 = async { delegate?.findById(it) }
                }

                val deferred2 = async { delegate?.findAllDepartments() }

                userVO = deferred1?.await()
                val depts = deferred2.await()

                (spinner.adapter as? ArrayAdapter<String>)?.addAll(depts?.values ?: arrayListOf())

                userVO?.department?.let {
                    spinner.setSelection(it.first.toInt())
                }
            }

            save.setOnClickListener {
                val user = UserVO(userVO?.id ?: 0, username.text.toString(), first.text.toString(), last.text.toString(),
                    email.text.toString(), password.text.toString(), Pair(spinner.selectedItemPosition.toLong(), spinner.selectedItem.toString()))

                if (user.password != confirm.text.toString()) {
                    fault(Exception(getString(R.string.error_password)))
                    return@setOnClickListener
                }

                if (!user.isValid()!!) {
                    fault(Exception(getString(R.string.error_invalid_data)))
                    return@setOnClickListener
                }

                if (!username.isEnabled) {
                    MainScope().launch(handler) {
                        delegate?.update(user, roles)?.let {
                            navController.previousBackStackEntry?.savedStateHandle?.set("userVO", user)
                            navController.popBackStack()
                        }
                    }
                    return@setOnClickListener
                }

                MainScope().launch(handler) {
                    delegate?.save(user, roles)?.let {
                        user.id = it
                        navController.previousBackStackEntry?.savedStateHandle?.set("userVO", user)
                        navController.popBackStack()
                    }
                }

            }

            cancel.setOnClickListener {
                navController.navigate(R.id.action_userForm_to_userList)
            }

            rolesButton.setOnClickListener {
                val userRole = UserRole()
                arguments?.getLong("id")?.let { id ->
                    userRole.arguments = bundleOf("id" to (id), "roles" to roles)
                } ?: run {
                    userRole.arguments = bundleOf( "roles" to roles)
                }
                userRole.setTargetFragment(this@UserForm, 1)
                userRole.show(parentFragmentManager.beginTransaction(), "dialog")
            }
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            roles = data?.extras?.getSerializable("roles") as HashMap<Long, String>
        } else {
            roles = null
        }
    }

    private fun fault(exception: Throwable) {
        Log.d("UserForm", "Error: ${exception.localizedMessage}")
        (activity as? EmployeeAdmin)?.fault(exception)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        fault(exception)
    }

    fun setDelegate(delegate: IUserForm) {
        this.delegate = delegate
    }

    interface IUserForm {
        suspend fun findById(id: Long): UserVO?
        suspend fun save(user: UserVO, roles: HashMap<Long, String>?): Long?
        suspend fun update(user: UserVO, roles: HashMap<Long, String>?): Int?
        suspend fun findAllDepartments(): HashMap<Long, String>?
    }

}