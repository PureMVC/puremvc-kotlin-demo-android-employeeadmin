//
//  UserRole.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserRoleBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import java.lang.ref.WeakReference

class UserRole: DialogFragment() {

    private val username by lazy { arguments?.getString("username") }

    private val roleEnumsArgs by lazy { arguments?.getParcelableArrayList<RoleEnum>("roleEnums") }

    private var roleEnums: ArrayList<RoleEnum>? = null

    private val delegate by lazy { WeakReference(activity as IUserRole) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = UserRoleBinding.inflate(inflater, container, false).apply {

            ok.setOnClickListener {
                dialog?.dismiss()
                targetFragment?.onActivityResult(1, Activity.RESULT_OK, Intent().putParcelableArrayListExtra("roleEnums", roleEnums))
            }

            cancel.setOnClickListener {
                dialog?.dismiss()
                targetFragment?.onActivityResult(1, Activity.RESULT_CANCELED, null)
            }

            username?.let { // existing user
                delegate.get()?.getUserRoles(it)?.let { // existing roles
                    roleEnums = it
                }
            }

            roleEnumsArgs?.let { // if roles are passed from a previous selection
                roleEnums = it.toMutableList() as ArrayList<RoleEnum> // copy array to avoid side effects
            }

            savedInstanceState?.let { // restore and override state if rotated
                roleEnums = it.getParcelableArrayList<RoleEnum>("roleEnums") as ArrayList<RoleEnum>
            }

            if (roleEnums == null) roleEnums = arrayListOf()

            listView.adapter = UserRoleAdapter(activity, RoleEnum.list())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
         dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putParcelableArrayList("roleEnums", roleEnums)
        super.onSaveInstanceState(bundle)
    }

    private inner class UserRoleAdapter(context: Context?, data: ArrayList<RoleEnum>) : ArrayAdapter<RoleEnum>(context!!, 0, data as List<RoleEnum>) {

        override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
            var view: View? = convertView
            if (view == null) {
                view = layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice, container, false)
            }

            val checkedTextView = view?.findViewById<CheckedTextView>(android.R.id.text1)
            checkedTextView?.text = getItem(position)?.name

            checkedTextView?.isChecked = roleEnums?.contains(getItem(position)) ?: false
            checkedTextView?.tag = getItem(position)

            checkedTextView?.setOnClickListener {
                if (checkedTextView.isChecked) {
                    checkedTextView.isChecked = false
                    roleEnums?.remove(it.tag)
                } else {
                    checkedTextView.isChecked = true
                    roleEnums?.add(it.tag as RoleEnum)
                }
            }
            return view!!
        }

    }

    interface IUserRole {
        fun getUserRoles(username: String): ArrayList<RoleEnum>?
    }

}