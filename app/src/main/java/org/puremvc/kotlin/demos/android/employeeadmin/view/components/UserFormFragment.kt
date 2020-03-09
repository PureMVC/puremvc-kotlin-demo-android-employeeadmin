package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_form_fragment.*
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

class UserFormFragment: Fragment() {

    private lateinit var user: UserVO

    private var roles: ArrayList<RoleEnum> = arrayListOf()

    private lateinit var delegate: IUserFormFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_form_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner.adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, DeptEnum.comboList())

        rolesButton.setOnClickListener { delegate.getRoles(roles) }

        save.setOnClickListener { _ -> // save or update
            val user = UserVO(username.text.toString(), first.text.toString(), last.text.toString(),
                email.text.toString(), password.text.toString(), spinner.selectedItem as DeptEnum)

            if (user.password != confirm.text.toString()) {
                AlertDialog.Builder(activity!!).setTitle("Error")
                    .setMessage("Your password and confirmation password do not match.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, null).create().show()
                return@setOnClickListener
            }

            if (!user.isValid()!!) {
                AlertDialog.Builder(activity!!).setTitle("Error")
                    .setMessage("Invalid Form Data.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, null).create().show()
                return@setOnClickListener
            }

            if (!getView()!!.findViewById<View>(R.id.username).isEnabled) {
                delegate.update(user, roles)
                return@setOnClickListener
            }

            delegate.save(user, roles)
        }

        cancel.setOnClickListener { _ ->
            delegate.cancel();
        }

    }

    fun setData(user: UserVO?, roles: ArrayList<RoleEnum>) {
        if (user == null) { return } // new user

        this.user = user
        this.roles = roles

        first.setText(user.first)
        last.setText(user.last)
        email.setText(user.email)
        username.setText(user.username)
        password.setText(user.password)
        confirm.setText(user.password)
        save.setText(R.string.update)

        username.isEnabled = false

        val adapter = spinner.adapter
        for (i in 1 until adapter.count) {
            if (adapter.getItem(i) === user.department) {
                spinner.setSelection(i)
                break
            }
        }
    }

    fun setRoles(roles: ArrayList<RoleEnum>) {
        this.roles = roles
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = activity as IUserFormFragment
    }

    interface IUserFormFragment {
        fun save(user: UserVO, roles: ArrayList<RoleEnum>)
        fun update(user: UserVO, roles: ArrayList<RoleEnum>)
        fun getRoles(roles: ArrayList<RoleEnum>)
        fun cancel()
    }

}