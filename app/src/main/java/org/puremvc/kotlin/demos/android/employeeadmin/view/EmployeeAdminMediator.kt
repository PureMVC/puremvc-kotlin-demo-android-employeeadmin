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
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.EmployeeAdmin
import org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces.IEmployeeAdmin
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class EmployeeAdminMediator(override var viewComponent: WeakReference<Any?>?): Mediator(NAME, viewComponent), IEmployeeAdmin {

    companion object {
        const val NAME: String = "UserListMediator"
    }

    private lateinit var userProxy: UserProxy

    private lateinit var roleProxy: RoleProxy

    override fun onRegister() {
        userProxy = facade.retrieveProxy(UserProxy.NAME) as UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as RoleProxy

        (viewComponent?.get() as EmployeeAdmin).setDelegate(this)
    }

    override fun getUsers(): ArrayList<UserVO> {
        return userProxy.users
    }

    override fun saveUser(user: UserVO, roleVO: RoleVO) {
        userProxy.addItem(user)
        roleProxy.addItem(roleVO)
    }

    override fun updateUser(user: UserVO, roleVO: RoleVO?) {
        userProxy.updateItem(user)
        roleVO?.let {
            roleProxy.updateUserRoles(it.username, it.roles)
        }
    }

    override fun deleteUser(username: String) {
        userProxy.deleteItem(username)
        roleProxy.deleteItem(username)
    }

    override fun getUserRoles(username: String): ArrayList<RoleEnum>? {
        return roleProxy.getUserRoles(username)
    }

}