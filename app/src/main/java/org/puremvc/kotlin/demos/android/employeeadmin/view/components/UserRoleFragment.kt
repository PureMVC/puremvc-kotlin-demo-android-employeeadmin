package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_role_fragment.*
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum

class UserRoleFragment: Fragment() {

    private lateinit var roles: ArrayList<RoleEnum>

    private lateinit var delegate: IUserRoleFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_role_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.adapter = UserRoleAdapter(activity, RoleEnum.list())
        ok.setOnClickListener { delegate.save(roles) }
        cancel.setOnClickListener { delegate.cancel() }
    }

    fun setRoles(roles: ArrayList<RoleEnum>) {
        this.roles = roles
    }

    private inner class UserRoleAdapter(context: Context?, data: ArrayList<RoleEnum>) : ArrayAdapter<RoleEnum>(context!!, 0, data as List<RoleEnum>) {

        override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice, container, false)
            }

            val checkedTextView = convertView?.findViewById<CheckedTextView>(android.R.id.text1)
            checkedTextView?.text = getItem(position)?.name

            checkedTextView?.isChecked = roles.contains(getItem(position))
            checkedTextView?.tag = getItem(position)

            checkedTextView?.setOnClickListener { view: View ->
                if (checkedTextView.isChecked) {
                    checkedTextView.isChecked = false
                    roles.remove(view.tag)
                } else {
                    checkedTextView.isChecked = true
                    roles.add(view.tag as RoleEnum)
                }
            }
            return convertView!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = activity as IUserRoleFragment
    }

    interface IUserRoleFragment {
        fun save(roles: ArrayList<RoleEnum>)
        fun cancel()
    }

}