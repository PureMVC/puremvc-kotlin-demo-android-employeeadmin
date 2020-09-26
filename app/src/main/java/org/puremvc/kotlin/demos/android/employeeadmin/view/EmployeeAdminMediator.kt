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
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserForm
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserList
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserRole
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class EmployeeAdminMediator(override var viewComponent: WeakReference<Any?>?): Mediator(NAME, viewComponent), UserList.IUserList, UserForm.IUserForm, UserRole.IUserRole {

    companion object {
        const val NAME: String = "EmployeeAdminMediator"
    }

    private var userProxy: UserProxy? = null
    private var roleProxy: RoleProxy? = null

    override fun onRegister() {
        userProxy = facade.retrieveProxy(UserProxy.NAME) as UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as RoleProxy

        when (val fragment = viewComponent?.get()) {
            is UserList -> fragment.setDelegate(this)
            is UserForm -> fragment.setDelegate(this)
            is UserRole -> fragment.setDelegate(this)
        }
    }

    override suspend fun findAll(): ArrayList<UserVO>? {
        return userProxy?.findAll()
    }

    override suspend fun deleteById(id: Long): Int? {
        return userProxy?.deleteById(id)
    }

    override suspend fun findById(id: Long): UserVO? {
        return userProxy?.findById(id)
    }

    override suspend fun save(user: UserVO, roles: HashMap<Long, String>?): Long?  {
        val id = userProxy?.save(user)
        roles?.let {
            roleProxy?.updateRolesByUserId(id!!, it)
        }
        return id
    }

    override suspend fun update(user: UserVO, roles: HashMap<Long, String>?): Int? {
        var id: Int? = null
        userProxy?.update(user)?.let {
            id = it
            roles?.let {
                roleProxy?.updateRolesByUserId(user.id, it)
            }
        }
        return id
    }

    override suspend fun findAllDepartments(): HashMap<Long, String>? {
        return userProxy?.findAllDepartments()
    }

    override suspend fun findAllRoles(): HashMap<Long, String>? {
        return roleProxy?.findAll()
    }

    override suspend fun findRolesById(id: Long): HashMap<Long, String>? {
        return roleProxy?.findAllByUserId(id)
    }

}