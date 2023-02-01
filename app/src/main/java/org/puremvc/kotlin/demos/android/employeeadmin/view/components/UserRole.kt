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
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.lang.ref.WeakReference

interface IUserRole {
    fun findAllRoles(): List<Role>
    fun findRolesById(id: Long): List<Role>
}

class UserRole: DialogFragment() {

    private var roles: ArrayList<Role>? = null

    private var _binding: UserRoleBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserRole? = null

    companion object {
        const val TAG = "UserRole"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserRoleBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e)?.show()
        }).launch { // Concurrent UI and User Data requests
            IdlingResource.increment()

            launch { // Get UI Data
                withContext(Dispatchers.IO) {
                    val items = delegate?.findAllRoles()?.map { it.name } ?: listOf()
                    withContext(Dispatchers.Main) { // Set UI Data
                        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_multiple_choice, items)
                        binding.listView.adapter = adapter
                    }
                }
            }

            arguments?.getParcelableArrayList("roles", Role::class.java)?.let { // Get User Data: Arguments
                roles = it.toMutableList() as ArrayList<Role> // Copy array to avoid side effects (passed by reference)
            }

            savedInstanceState?.let { // Get User Data: Cache
                roles = it.getParcelableArrayList("roles", Role::class.java)
            }

            launch { // Get User Data: IO
                roles ?: run {
                    arguments?.getLong("id")?.let { id -> // default 0L
                        withContext(Dispatchers.IO) {
                            roles = if(id != 0L) delegate?.findRolesById(id) as ArrayList<Role>? else arrayListOf() // default
                        }
                    }
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE
            roles?.forEach { // Set User Data
                binding.listView.setItemChecked(it.id?.toInt()?.minus(1) ?: 0, true)
            }
            IdlingResource.decrement()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            btnOk.setOnClickListener {
                dialog?.dismiss()
                setFragmentResult("roles", bundleOf("roles" to roles))
            }
            btnCancel.setOnClickListener { dialog?.dismiss() }
            listView.setOnItemClickListener { parent, view, position, id ->
                toggleRole(parent, view, position, id)
            }
        }
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
        ApplicationFacade.getInstance(ApplicationFacade.KEY).remove(WeakReference(this))
    }

    fun setDelegate(delegate: IUserRole) {
        this.delegate = delegate
    }

}