//
//  UserListActivity.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_list_activity.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces.IUserListActivity

class UserListActivity: AppCompatActivity(), UserListFragment.IUserListFragment {

    private lateinit var delegate: IUserListActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_list_activity)
        supportActionBar?.title = "User List"

        (application as Application).registerActivity(this)

        (fragment as UserListFragment).setUsers(delegate.getUsers())
    }

    override fun getDetails(user: UserVO?) { // UserFormActivity Request
        val intent = Intent(this, UserFormActivity::class.java).apply {
            putExtra(Application.BUNDLE_USER, user)
            if (user != null) {
                putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, delegate.getUserRoles(user.username))
            } else {
                putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, ArrayList<RoleEnum>())
            }
        }
        startActivityForResult(intent, Application.ACTIVITY_USER_FORM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // UserFormActivity Result
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Application.ACTIVITY_USER_FORM) {
            if (resultCode == Activity.RESULT_FIRST_USER) { // add
                val user = data?.getParcelableExtra<UserVO>(Application.BUNDLE_USER)
                val roles = data?.getParcelableArrayListExtra<RoleEnum>(Application.BUNDLE_USER_ROLE)
                (fragment as UserListFragment).addUser(user!!)
                delegate.saveUser(user, roles!!)
            } else if (resultCode == Activity.RESULT_OK) { // update
                val user = data?.getParcelableExtra<UserVO>(Application.BUNDLE_USER)
                val roles = data?.getParcelableArrayListExtra<RoleEnum>(Application.BUNDLE_USER_ROLE)
                (fragment as UserListFragment).updateUser(user!!)
                delegate.updateUser(user)
                delegate.updateUserRoles(user.username, roles!!)
            } else { // cancel
            }
        }
    }

    override fun delete(userVO: UserVO) {
        delegate.deleteUser(userVO)
    }

    fun setDelegate(delegate: IUserListActivity) {
        this.delegate = delegate
    }

}
