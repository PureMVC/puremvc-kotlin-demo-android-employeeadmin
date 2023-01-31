//
//  EmployeeAdminMediator.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

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
        userProxy = facade.retrieveProxy(UserProxy.NAME) as UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as RoleProxy

        when (val view = viewComponent?.get()) {
            is UserList -> view.setDelegate(this)
            is UserForm -> view.setDelegate(this)
            is UserRole -> view.setDelegate(this)
        }
    }

    override fun findAll(): ArrayList<User> {
        return userProxy?.findAll() ?: arrayListOf()
    }

    override fun deleteById(id: Long): Int {
        return userProxy?.deleteById(id) ?: 0
    }

    override fun findById(id: Long): User? {
        return userProxy?.findById(id)
    }

    override fun save(user: User, roles: List<Role>?): Long  {
        val id = userProxy?.save(user)?.also { id ->
            roles?.let { roleProxy?.updateByUserId(id, it) }
        }
        return id ?: 0
    }

    override fun update(user: User, roles: List<Role>?): Int {
        val modified = userProxy?.update(user)?.also {
            roleProxy?.updateByUserId(user.id!!, roles)
        }
        return modified ?: 0
    }

    override fun findAllDepartments(): List<Department> {
        return userProxy?.findAllDepartments() ?: arrayListOf()
    }

    override fun findAllRoles(): List<Role> {
        return roleProxy?.findAll() ?: arrayListOf()
    }

    override fun findRolesById(id: Long): ArrayList<Role> {
        return roleProxy?.findByUserId(id) ?: arrayListOf()
    }

}