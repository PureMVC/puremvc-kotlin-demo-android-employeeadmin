//
//  EmployeeAdminMediator.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

import androidx.lifecycle.LiveData
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.*
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class EmployeeAdminMediator(name: String, override var viewComponent: WeakReference<*>?): Mediator(name, viewComponent), IUserList, IUserForm, IUserRole {

    companion object {
        const val NAME: String = "EmployeeAdminMediator"
    }

    private var userProxy: UserProxy? = null
    private var roleProxy: RoleProxy? = null

    override fun onRegister() {
        userProxy = facade.retrieveProxy(UserProxy.NAME) as? UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as? RoleProxy

        when (val view = viewComponent?.get()) {
            is UserList -> view.setDelegate(this)
            is UserForm -> view.setDelegate(this)
            is UserRole -> view.setDelegate(this)
        }
    }

    override suspend fun findAll(): LiveData<List<User>>? {
        return userProxy?.findAll()
    }

    override suspend fun deleteById(id: Long): Int? {
        return userProxy?.deleteById(id)
    }

    override suspend fun findById(id: Long): Map<User, Department>? {
        return userProxy?.findById(id)
    }

    override suspend fun save(user: User, roles: List<Role>?): Long?  {
        val id = userProxy?.save(user)?.also { id ->
            roles?.let {
                roleProxy?.insertUserRoles(id, it)
            }
        }
        return id
    }

    override suspend fun update(user: User, roles: List<Role>?): Int? {
        val affected = userProxy?.update(user)?.also {
            roles?.let {
                roleProxy?.updateByUserId(user.id, it)
            }
        }
        return affected
    }

    override suspend fun findAllDepartments(): List<Department>? {
        return userProxy?.findAllDepartments()
    }

    override suspend fun findAllRoles(): List<Role>? {
        return roleProxy?.findAll()
    }

    override suspend fun findRolesById(id: Long): List<Role>? {
        return roleProxy?.findByUserId(id)
    }

}