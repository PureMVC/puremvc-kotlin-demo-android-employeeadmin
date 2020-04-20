//
//  IEmployeeAdmin.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.interfaces

import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

interface IEmployeeAdmin {

    fun getUsers(): ArrayList<UserVO>

    fun deleteUser(username: String)

    fun saveUser(user: UserVO, roleVO: RoleVO?)

    fun updateUser(user: UserVO, roleVO: RoleVO?)

    fun getUserRoles(username: String): ArrayList<RoleEnum>?

}