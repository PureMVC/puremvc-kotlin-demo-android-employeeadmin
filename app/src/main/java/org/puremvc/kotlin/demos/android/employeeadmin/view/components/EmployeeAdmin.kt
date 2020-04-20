//
//  EmployeeAdmin.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces.IEmployeeAdmin
import java.lang.ref.WeakReference

class EmployeeAdmin: AppCompatActivity(), UserList.IUserList, UserForm.IUserForm, UserRole.IUserRole {

    private lateinit var delegate: IEmployeeAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_admin)
        supportActionBar?.title = "Employee Admin"
        (application as Application).registerActivity(WeakReference(this))
    }

    override fun getUsers(): ArrayList<UserVO> {
        return delegate.getUsers()
    }

    override fun delete(username: String) {
        delegate.deleteUser(username)
    }

    override fun save(user: UserVO, roleVO: RoleVO?) {
        delegate.saveUser(user, roleVO)
    }

    override fun update(user: UserVO, roleVO: RoleVO?) {
        delegate.updateUser(user, roleVO)
    }

    override fun getUserRoles(username: String): ArrayList<RoleEnum>? {
        return delegate.getUserRoles(username)
    }

    fun setDelegate(delegate: IEmployeeAdmin) {
        this.delegate = delegate
    }

}
