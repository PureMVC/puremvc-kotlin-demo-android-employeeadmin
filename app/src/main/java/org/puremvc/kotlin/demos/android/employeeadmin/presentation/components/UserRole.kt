//
//  UserRole.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.presentation.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Role
import org.puremvc.kotlin.demos.android.employeeadmin.presentation.MainViewModel

@AndroidEntryPoint
class UserRole: DialogFragment() {

    private var roles: ArrayList<Role>? = null

    private lateinit var viewModel: MainViewModel

    private var _binding: UserRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserRoleBinding.inflate(inflater, container, false)

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }) {
            IdlingResource.increment()

            val items = viewModel.findAllRoles() ?: listOf() // UI Data
            val adapter = RoleAdapter(requireActivity(), ArrayList(items))
            binding.listView.adapter = adapter

            arguments?.getParcelableArrayList("roles", Role::class.java)?.let { // Get User Data: Arguments
                roles = it.toMutableList() as? ArrayList<Role> // Copy array to avoid side effects (passed by reference)
            } ?: run { // Get User Data: IO
                arguments?.getInt("id")?.let { id -> // default 0
                    roles = if(id != 0) viewModel.findRolesById(id) as ArrayList<Role>? else arrayListOf() // or default
                }
            }

            binding.progressBar.visibility = View.GONE
            roles?.forEach { // Set User Data
                binding.listView.setItemChecked(it.id.toInt().minus(1), true)
            }

            IdlingResource.decrement()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) { // Set Event Handlers
            listView.setOnItemClickListener { _, view, _, _ ->
                (view.tag as? RoleAdapter.ViewHolder)?.let { toggle(it) }
            }

            btnOk.setOnClickListener {
                dialog?.dismiss()
                setFragmentResult("roles", bundleOf("roles" to roles))
            }

            btnCancel.setOnClickListener { dialog?.dismiss() }
        }
    }

    private fun toggle(holder: RoleAdapter.ViewHolder) {
        when(holder.checkedTextView.isChecked) {
            true -> roles?.add(holder.role)
            else -> roles?.remove(holder.role)
        }
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

private class RoleAdapter(context: Context, roles: ArrayList<Role>):
    ArrayAdapter<Role>(context, android.R.layout.simple_list_item_multiple_choice, roles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)

        val viewHolder = convertView?.tag as? ViewHolder ?: ViewHolder(view)
        getItem(position)?.let { viewHolder.bind(it) }
        view.tag = viewHolder

        return view
    }

    class ViewHolder(val view: View) {

        lateinit var role: Role
        val checkedTextView = view.findViewById<CheckedTextView>(android.R.id.text1)!!

        fun bind(role: Role) {
            this.role = role
            checkedTextView.text = role.name
        }
    }
}