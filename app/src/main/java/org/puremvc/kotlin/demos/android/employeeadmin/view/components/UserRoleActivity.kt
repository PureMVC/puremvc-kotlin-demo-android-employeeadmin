package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_list_activity.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum

class UserRoleActivity: AppCompatActivity(), UserRoleFragment.IUserRoleFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_role_activity)
        supportActionBar?.title = "User Role"

        if (savedInstanceState == null) {
            (fragment as UserRoleFragment).setRoles(intent.getParcelableArrayListExtra(Application.BUNDLE_USER_ROLE))
        }
    }

    override fun save(roles: ArrayList<RoleEnum>) {
        val intent = Intent().apply {
            putParcelableArrayListExtra(Application.BUNDLE_USER_ROLE, roles)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}