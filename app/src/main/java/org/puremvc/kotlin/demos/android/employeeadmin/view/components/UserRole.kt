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
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import java.lang.ref.WeakReference

interface IUserRole {
    fun getUserRoles(username: String): ArrayList<RoleEnum>?
}

class UserRole: DialogFragment(), AdapterView.OnItemClickListener {

    private var roles: ArrayList<RoleEnum>? = null

    private var _binding: UserRoleBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).registerView(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserRoleBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_multiple_choice, RoleEnum.values()) // Get UI Data
        binding.listView.adapter = adapter // Set UI Data

        arguments?.getParcelableArrayList("roles", RoleEnum::class.java)?.let { // Get User Data: Previous Selection
            roles = it.toMutableList() as ArrayList<RoleEnum> // copy array to avoid side effects (passed by reference)
        }

        savedInstanceState?.let { // Get User Data: Cache
            roles = it.getParcelableArrayList("roles", RoleEnum::class.java)
        }

        roles ?: run { // Get User Data: IO
            arguments?.getString("username")?.let {
                roles = delegate?.getUserRoles(it)
            }
        }

        roles = roles ?: arrayListOf() // Set User Data: Default
        roles?.forEach { // Set User Data
            binding.listView.setItemChecked(it.ordinal, true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            btnOk.setOnClickListener {
                setFragmentResult("roles", bundleOf("roles" to roles))
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener { dialog?.dismiss() }
            listView.onItemClickListener = this@UserRole
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.findViewById<CheckedTextView>(android.R.id.text1)?.isChecked) {
            true -> {
                RoleEnum.values().find { it.ordinal == position }?.let { roles?.add(it) }
            }
            else -> {
                roles?.removeIf { it.ordinal == position }
            }
        }
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onSaveInstanceState(bundle: Bundle) { // Set Data: Cache
        bundle.putParcelableArrayList("roles", roles)
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