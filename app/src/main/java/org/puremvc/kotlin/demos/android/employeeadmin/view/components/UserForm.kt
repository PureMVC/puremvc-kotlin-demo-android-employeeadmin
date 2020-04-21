//
//  UserForm.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserFormBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import java.lang.ref.WeakReference

class UserForm: Fragment() {

    private val userVO by lazy { arguments?.getParcelable<UserVO>("userVO") }

    private var roleEnums: ArrayList<RoleEnum>? = null

    private lateinit var navController: NavController

    private val delegate by lazy { WeakReference(activity as IUserForm) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = UserFormBinding.inflate(inflater, container, false).apply {
            userVO = this@UserForm.userVO
            val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, DeptEnum.comboList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departments = adapter

            spinner.post {
                this@UserForm.userVO?.let {
                    for (i in 1 until adapter.count) {
                        if (adapter.getItem(i) === it.department) {
                            spinner.setSelection(i)
                            break
                        }
                    }
                }
            }

            saveListener = View.OnClickListener {
                val userVO = UserVO(username.text.toString(), first.text.toString(), last.text.toString(),
                    email.text.toString(), password.text.toString(), spinner.selectedItem as DeptEnum)

                if (userVO.password != confirm.text.toString()) {
                    AlertDialog.Builder(activity!!)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.error_password))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .create().show()
                    return@OnClickListener
                }

                if (!userVO.isValid()!!) {
                    AlertDialog.Builder(activity!!)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.error_invalid_data))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .create().show()
                    return@OnClickListener
                }

                if (!username.isEnabled) {
                    val roleVO = if (roleEnums != null) RoleVO(userVO.username, roleEnums!!) else null
                    delegate.get()?.update(userVO, roleVO)
                    activity?.onBackPressed()
                    return@OnClickListener
                }

                delegate.get()?.save(userVO, RoleVO(userVO.username, roleEnums ?: arrayListOf()))
                activity?.onBackPressed()
            }

            cancelListener = View.OnClickListener {
                activity?.onBackPressed()
            }

            rolesListener = View.OnClickListener {
                val userRole = UserRole()
                userRole.arguments = bundleOf("username" to (userVO?.username), "roleEnums" to roleEnums)
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
        roleEnums = data?.getParcelableArrayListExtra("roleEnums")
    }

    interface IUserForm {
        fun save(user: UserVO, roleVO: RoleVO)
        fun update(user: UserVO, roleVO: RoleVO?)
    }

}