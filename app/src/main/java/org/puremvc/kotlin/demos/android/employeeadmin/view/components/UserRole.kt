//
//  UserRole.kt
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.lang.ref.WeakReference

interface IUserRole {
    fun findAllRoles(): List<Role>?
    fun findRolesById(id: Long?): ArrayList<Role>?
}

class UserRole: DialogFragment() {

    private var roles: ArrayList<Role>? = null

    private var _binding: UserRoleBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).registerView(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserRoleBinding.inflate(inflater, container, false)

        IdlingResource.increment()
        val handler = CoroutineExceptionHandler { _, e -> (activity as? EmployeeAdmin)?.alert(e)?.show() }
        lifecycleScope.launch(handler) { // Concurrent UI and User Data requests
            launch { // Get UI Data
                withContext(Dispatchers.IO) {
                    val items = delegate?.findAllRoles()?.map { it.name } ?: listOf()
                    withContext(Dispatchers.Main) { // Bind UI Data
                        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_multiple_choice, items)
                        binding.listView.adapter = adapter
                    }
                }
            }

            arguments?.getParcelableArrayList("roles", Role::class.java)?.let { // Get User Data: Previous selection
                roles = it.toMutableList() as ArrayList<Role> // Copy array to avoid side effects (passed by reference)
            }

            savedInstanceState?.let { // Get User Data: State restoration
                roles = it.getParcelableArrayList("roles", Role::class.java)
            }

            roles ?: run { // Get User Data: Network
                arguments?.getLong("id")?.let {
                    launch {
                        withContext(Dispatchers.IO) {
                            roles = delegate?.findRolesById(if(it != 0L) it else null)
                        }
                    }
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE
            roles = roles ?: arrayListOf() // Default User Data
            roles?.forEach { // Bind User Data
                binding.listView.setItemChecked(it.id?.toInt()?.minus(1) ?: 0, true)
            }

            binding.apply { // Bind Event Handlers
                btnOk.setOnClickListener {
                    dialog?.dismiss()
                    setFragmentResult("roles", bundleOf("roles" to roles))
                }
                btnCancel.setOnClickListener { dialog?.dismiss() }
                listView.setOnItemClickListener { parent, view, position, id ->
                    toggleRole(parent, view, position, id)
                }
            }

            IdlingResource.decrement()
        }

        return binding.root
    }

    private fun toggleRole(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (view.findViewById<CheckedTextView>(android.R.id.text1)?.isChecked) {
            true -> roles?.add(Role(id + 1, parent.adapter.getItem(position).toString()))
            else -> roles?.removeIf { it.id == id + 1 }
        }
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putSerializable("roles", roles)
        super.onSaveInstanceState(bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setDelegate(delegate: IUserRole) {
        this.delegate = delegate
    }

}