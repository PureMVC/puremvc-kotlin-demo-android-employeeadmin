//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class RoleProxy: Proxy(NAME, ArrayList<RoleVO>()) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    fun addItem(roleVO: RoleVO) {
        roles.add(roleVO)
    }

    fun getUserRoles(username: String): ArrayList<RoleEnum>? {
        var list: ArrayList<RoleEnum>? = null
        for (i in 0 until roles.size) {
            if (roles[i].username == username) {
                list = roles[i].roles
                break
            }
        }
        return list
    }

    fun updateUserRoles(username: String, role: ArrayList<RoleEnum>) {
        for (i in 0 until roles.size) {
            if (roles[i].username == username) {
                roles[i].roles = role
                break
            }
        }
    }

    fun deleteItem(username: String) {
        for(i in 0 until roles.size) {
            if (roles[i].username == username) {
                roles.removeAt(i)
                break
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    val roles: ArrayList<RoleVO> get() = data as ArrayList<RoleVO>

}