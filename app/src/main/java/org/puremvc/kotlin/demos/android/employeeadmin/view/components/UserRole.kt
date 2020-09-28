//
//  UserRole.kt
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
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.lang.ref.WeakReference

class UserRole: DialogFragment() {

    private var roles: ArrayList<Role>? = null

    private var delegate: IUserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as Application).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = UserRoleBinding.inflate(inflater, container, false).apply {

            MainScope().launch(handler) {
                // UI data
                val deferred1 = async { delegate?.findAllRoles() }

                // User data
                // arguments: previous role selection
                arguments?.getSerializable("roles")?.let {
                    roles = it as ArrayList<Role>
                }

                // cache: restore role selection
                savedInstanceState?.let {
                    roles = it.getSerializable("roles") as ArrayList<Role>
                }

                // network/database: existing user
                var deferred2: Deferred<ArrayList<Role>?>? = null
                if (roles == null) { // cache miss
                    arguments?.getLong("id")?.let { id ->
                        deferred2 = async { delegate?.findRolesById(id) }
                    }
                }

                // Await on both UI and User Data concurrent fetch
                val list = deferred1.await() ?: listOf()
                if (roles == null) roles = deferred2?.await() ?: arrayListOf<Role>()

                // Bind UI Data to UI
                listView.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_multiple_choice, list.map { it.name })

                // Bind User Data to UI
                roles?.forEach { role ->
                    listView.setItemChecked(role.id.toInt() - 1, true)
                }
            }

            // Event handlers
            listView.setOnItemClickListener { parent, view, position, id ->
                if (view?.findViewById<CheckedTextView>(android.R.id.text1)?.isChecked == true) {
                    roles?.add(Role(id + 1, parent.adapter.getItem(position).toString()))
                } else {
                    roles?.removeIf {
                        it.id == id + 1
                    }
                }
            }

            ok.setOnClickListener {
                dialog?.dismiss()
                targetFragment?.onActivityResult(1, Activity.RESULT_OK, Intent().putExtra("roles", roles))
            }

            cancel.setOnClickListener {
                dialog?.dismiss()
                targetFragment?.onActivityResult(1, Activity.RESULT_CANCELED, null)
            }

        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putSerializable("roles", roles) // cache
        super.onSaveInstanceState(bundle)
    }

    private fun fault(exception: Throwable) {
        Log.d("UserRole", "Error: ${exception.localizedMessage}")
        (activity as? EmployeeAdmin)?.fault(exception)
    }

    private val handler = CoroutineExceptionHandler { _, exception -> fault(exception) }

    fun setDelegate(delegate: IUserRole) {
        this.delegate = delegate
    }

    interface IUserRole {
        suspend fun findAllRoles(): List<Role>?
        suspend fun findRolesById(id: Long): ArrayList<Role>?
    }

}

/*
UI Pattern with Concurrency (1 & 2 in Parallel)
    1. UI Data (hardcoded, Network/Database || Default)
    2. User Data (Arguments/Cache, Network/Database on cache miss || Default) = field/variable
    3. Bind UI & User Data to UI
    4. Event Handlers
 */