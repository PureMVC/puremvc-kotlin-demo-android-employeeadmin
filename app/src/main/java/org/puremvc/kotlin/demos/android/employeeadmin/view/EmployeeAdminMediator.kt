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

class EmployeeAdminMediator(override var viewComponent: WeakReference<Any?>?): Mediator(NAME, viewComponent), IUserList, IUserForm, IUserRole {

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

    override fun findAll(): ArrayList<User>? {
        return null
        //return userProxy?.findAll()
    }

    override fun deleteById(id: Long?): Int? {
        id?.let {
            return userProxy?.deleteById(id)
        } ?: run {
            return null
        }
    }

    override fun findById(id: Long?): User? {
        id?.let {
            return userProxy?.findById(id)
        } ?: run {
            return null
        }
    }

    override fun save(user: User?, roles: List<Role>?): Long?  {
        user?.let {
            val id = userProxy?.save(user)

            id?.let {
                roles?.let {
                    roleProxy?.updateByUserId(id, it)
                }
            }
            return id
        } ?: run {
            return null
        }
    }

    override fun update(user: User?, roles: List<Role>?): Int? {
        user?.let {
            val modified = userProxy?.update(user)

            roles?.let {
                roleProxy?.updateByUserId(user.id!!, roles)
            }
            return modified
        } ?: run {
            return null
        }
    }

    override fun findAllDepartments(): List<Department>? {
        return userProxy?.findAllDepartments()
    }

    override fun findAllRoles(): List<Role>? {
        return roleProxy?.findAll()
    }

    override fun findRolesById(id: Long?): ArrayList<Role>? {
        id?.let {
            return roleProxy?.findByUserId(id)
        } ?: run {
            return null
        }
    }

}