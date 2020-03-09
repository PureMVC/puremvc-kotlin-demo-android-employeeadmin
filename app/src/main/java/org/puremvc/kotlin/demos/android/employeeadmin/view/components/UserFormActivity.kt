package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_form_activity.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

class UserFormActivity: AppCompatActivity(), UserFormFragment.IUserFormFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_form_activity)
        actionBar?.title = "User Form"

        if (savedInstanceState == null) {
            (fragment as UserFormFragment).setData(intent.getParcelableExtra(Application.BUNDLE_USER),
                intent.getParcelableArrayListExtra(Application.BUNDLE_USER_ROLE))
        }
    }

    override fun save(user: UserVO, roles: ArrayList<RoleEnum>) {
        val intent = Intent().apply {
            putExtra(Application.BUNDLE_USER, user)
            putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, roles)
        }
        setResult(Activity.RESULT_FIRST_USER, intent)
        finish()
    }

    override fun update(user: UserVO, roles: ArrayList<RoleEnum>) {
        val intent = Intent().apply {
            putExtra(Application.BUNDLE_USER, user)
            putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, roles)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun getRoles(roles: ArrayList<RoleEnum>) { // UserRoleActivity Request
        val intent = Intent(this, UserRoleActivity::class.java).apply {
            putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, roles)
        }
        startActivityForResult(intent, Application.ACTIVITY_USER_ROLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // UserRoleActivity Result
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Application.ACTIVITY_USER_ROLE) {
            if (resultCode == Activity.RESULT_OK) {
                (fragment as UserFormFragment).setRoles(data?.getParcelableArrayListExtra(Application.BUNDLE_USER_ROLE)!!)
            }
        }
    }

    override fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}