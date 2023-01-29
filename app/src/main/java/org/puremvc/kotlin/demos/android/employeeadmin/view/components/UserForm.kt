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
import androidx.navigation.fragment.findNavController
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserForm {
    fun findByUsername(username: String): User?
    fun save(user: User, role: Role?) // role - Secondary/Optional User Data
    fun update(user: User, role: Role?)
}

class UserForm: Fragment() {

    private var user: User? = null

    private var roles: ArrayList<RoleEnum>? = null

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

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, DeptEnum.values()) // Get UI Data
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter // Set UI Data

        savedInstanceState?.let { // Get User Data: Cache
            user = it.getParcelable("user", User::class.java)
            roles = it.getParcelableArrayList("roles", RoleEnum::class.java)
        }

        user ?: run { // Get User Data: IO
            arguments?.getString("username")?.let { username ->
                user = delegate?.findByUsername(username) // Set User Data: IO
                binding.username.isEnabled = false
            }
        }

        user ?: run { user = User() } // Set User Data: Default
        binding.user = user // Set User Data
        user?.department?.let { binding.spinner.setSelection(it.ordinal) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("roles", this@UserForm::onFragmentResult) // Set Event Handlers
        binding.apply {
            btnSave.setOnClickListener { save() }
            btnCancel.setOnClickListener { findNavController().navigate(R.id.action_userForm_to_userList) }
            btnRoles.setOnClickListener {
                arrayListOf(btnSave, btnCancel, btnRoles).forEach{ it.isEnabled = false } // Disable interactivity
                Handler(Looper.getMainLooper()).postDelayed({
                    arrayListOf(btnSave, btnCancel, btnRoles).forEach{ it.isEnabled = true } }, 3000)
                selectRoles()
            }
        }
    }

    private fun save() {
        binding.apply { // Set Data: Initialization
            user = User(username.text.toString(), first.text.toString(), last.text.toString(),
                email.text.toString(), password.text.toString(), spinner.selectedItem as DeptEnum)

            user?.validate(confirm.text.toString())?.let { // Set Data: Validation
                (activity as? EmployeeAdmin)?.alert(Exception(it))?.show()
                return
            }

            if (username.isEnabled) { // Set Data: IO
                delegate?.save(user!!, if(roles?.size?.compareTo(0) == 1) Role(user!!.username, roles) else null)
            } else {
                delegate?.update(user!!, if(roles?.size?.compareTo(0) == 1) Role(user!!.username, roles) else null)
            }

            user?.let { viewModel.setUser(it) } // Set Data: View
            findNavController().navigate(R.id.action_userForm_to_userList) // Set Data: Post Action
        }
    }

    private fun selectRoles() {
        val userRole = UserRole()  // Get Data: View Initialization
        arguments?.getString("username")?.let { username ->
            userRole.arguments = bundleOf("username" to username, "roles" to roles)
        } ?: run {
            userRole.arguments = bundleOf( "roles" to roles)
        }
        userRole.show(parentFragmentManager.beginTransaction(), "dialog") // Get Data: View
    }

    private fun onFragmentResult(requestKey: String, bundle: Bundle) { // Set Data: View
        when (requestKey) {
            "roles" -> roles = bundle.getParcelableArrayList("roles", RoleEnum::class.java)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) { // Set User Data: Cache
        outState.putParcelable("user", binding.user)
        roles?.let { outState.putParcelableArrayList("roles", it) }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setDelegate(delegate: IUserForm) {
        this.delegate = delegate
    }

}