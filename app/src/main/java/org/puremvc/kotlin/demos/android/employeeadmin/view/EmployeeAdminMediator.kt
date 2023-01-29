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
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.*
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class EmployeeAdminMediator(override var name: String, override var viewComponent: WeakReference<*>?): Mediator(name, viewComponent), IUserList, IUserForm, IUserRole {

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

    override fun findAll(): ArrayList<User>? {
        return userProxy?.users
    }

    override fun findByUsername(username: String): User? {
        return userProxy?.findByUsername(username)
    }

    override fun save(user: User, role: Role?) {
        userProxy?.addItem(user)
        role?.let {
            roleProxy?.addItem(it)
        }
    }

    override fun update(user: User, role: Role?) {
        userProxy?.updateItem(user)
        role?.let {
            roleProxy?.updateUserRoles(it.username, it.roles)
        }
    }

    override fun deleteByUsername(username: String) {
        userProxy?.deleteItem(username)
        roleProxy?.deleteItem(username)
    }

    override fun getUserRoles(username: String): ArrayList<RoleEnum>? {
        return roleProxy?.getUserRoles(username)
    }

}