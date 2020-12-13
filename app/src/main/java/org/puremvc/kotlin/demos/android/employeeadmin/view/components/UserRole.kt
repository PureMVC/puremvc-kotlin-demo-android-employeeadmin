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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

interface IUserRole {
    fun findAllRoles(): List<Role>?
    fun findRolesById(id: Long?): ArrayList<Role>?
}

class UserRole: DialogFragment() {

    private var roles: ArrayList<Role>? = null

    private var delegate: IUserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as Application).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = UserRoleBinding.inflate(inflater, container, false).apply {
            btnOk.setOnClickListener { ok() }
            btnCancel.setOnClickListener { cancel() }
            listView.setOnItemClickListener { parent, view, position, id ->
                toggleRole(parent, view, position, id)
            }
        }

        var list: List<Role>? = null

        IdlingResource.increment()
        lifecycleScope.launch(handler) {

            launch {
                withContext(Dispatchers.IO) {
                    list = delegate?.findAllRoles() // UI data
                }
            }

            // User data
            arguments?.getSerializable("roles")?.let { // arguments: previous role selection
                if (it is List<*>) {
                    roles = ArrayList(it.filterIsInstance<Role>())
                }
            }

            savedInstanceState?.let { // cache
                val obj = it.getSerializable("roles")
                if (obj is List<*>) {
                    roles = ArrayList(obj.filterIsInstance<Role>())
                }
            }

            if (roles == null) { // cache miss
                arguments?.getLong("id")?.let {
                    launch { // network/database: existing user
                        withContext(Dispatchers.IO) {
                            roles = delegate?.findRolesById(if(it != 0L) it else null)
                        }
                    }
                }
            }

        }.invokeOnCompletion { // Stitch/Bind UI and User Data
            binding.listView.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_multiple_choice, list?.map { it.name } ?: listOf()) // Bind UI Data to UI

            if (roles == null) roles = arrayListOf()
            roles?.forEach { // Bind User Data to UI
                binding.listView.setItemChecked(it.id?.toInt()?.minus(1) ?: 0, true)
            }
            IdlingResource.decrement()
        }

        return binding.root
    }

    private fun ok() {
        dialog?.dismiss()
        targetFragment?.onActivityResult(1, Activity.RESULT_OK, Intent().putExtra("roles", roles))
    }

    private fun cancel() {
        dialog?.dismiss()
        targetFragment?.onActivityResult(1, Activity.RESULT_CANCELED, null)
    }

    private fun toggleRole(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (view.findViewById<CheckedTextView>(android.R.id.text1)?.isChecked == true) {
            roles?.add(Role(id + 1, parent.adapter.getItem(position).toString()))
        } else {
            roles?.removeIf {
                it.id == id + 1
            }
        }
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putSerializable("roles", roles) // cache
        super.onSaveInstanceState(bundle)
    }

    private fun fault(context: CoroutineContext, exception: Throwable) {
        Log.d("UserRole", "Error: ${exception.localizedMessage} $context")
        (activity as? EmployeeAdmin)?.fault(exception)
    }

    private val handler = CoroutineExceptionHandler { context, exception -> fault(context, exception) }

    fun setDelegate(delegate: IUserRole) {
        this.delegate = delegate
    }

}

/*
UI Pattern with Concurrency (2 & 3 in Parallel)
    1. Event Handlers within binding to pass bounded data
    2. UI Data (hardcoded, Network/Database || Default)
    3. User Data (Arguments/Cache hit, Network/Database on cache miss || Default) = field/variable
    4. Stitch UI & User Data to binding
 */