//
//  UserListMediator.kt
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
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserListActivity
import org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces.IUserListActivity
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator

class UserListMediator(override var viewComponent: Any?): Mediator(NAME, viewComponent), IUserListActivity {

    companion object {
        const val NAME: String = "UserListMediator"
    }

    private lateinit var userProxy: UserProxy

    private lateinit var roleProxy: RoleProxy

    override fun onRegister() {
        userProxy = facade.retrieveProxy(UserProxy.NAME) as UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as RoleProxy

        (viewComponent as UserListActivity).setDelegate(this)
    }

    override fun getUsers(): ArrayList<UserVO> {
        return userProxy.users()
    }

    override fun saveUser(user: UserVO, roles: ArrayList<RoleEnum>) {
        userProxy.addItem(user)
        roleProxy.addItem(RoleVO(user.username, roles))
    }

    override fun updateUser(user: UserVO) {
        userProxy.updateItem(user)
    }

    override fun deleteUser(userVO: UserVO) {
        userProxy.deleteItem(userVO)
        roleProxy.deleteItem(userVO.username)
    }

    override fun getUserRoles(username: String): ArrayList<RoleEnum> {
        return roleProxy.getUserRoles(username)
    }

    override fun updateUserRoles(username: String, roles: ArrayList<RoleEnum>) {
        roleProxy.updateUserRoles(username, roles)
    }

}