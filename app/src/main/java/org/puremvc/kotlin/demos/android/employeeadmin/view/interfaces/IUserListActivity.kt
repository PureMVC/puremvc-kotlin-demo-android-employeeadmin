//
//  IUserListActivity.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces

import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

interface IUserListActivity {

    fun getUsers(): ArrayList<UserVO>

    fun saveUser(user: UserVO, roles: ArrayList<RoleEnum>)

    fun updateUser(user: UserVO)

    fun deleteUser(userVO: UserVO)

    fun getUserRoles(username: String): ArrayList<RoleEnum>

    fun updateUserRoles(username: String, roles: ArrayList<RoleEnum>)

}