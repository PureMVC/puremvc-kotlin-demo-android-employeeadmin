//
//  RoleProxy.java
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
        roles().add(roleVO)
    }

    fun getUserRoles(username: String): ArrayList<RoleEnum> {
        var list = ArrayList<RoleEnum>()
        for (i in 0 until roles().size) {
            if (roles()[i].username == username) {
                list = roles()[i].roles;
                break;
            }
        }
        return list
    }

    fun updateUserRoles(username: String, roles: ArrayList<RoleEnum>) {
        val list = roles()
        for (i in 0 until list.size) {
            if (list[i].username == username) {
                list[i].roles = roles
                break
            }
        }
    }

    fun deleteItem(username: String) {
        val list = roles()
        for(i in 0 until list.size) {
            if (list[i].username == username) {
                list.removeAt(i)
                break
            }
        }
    }

    private fun roles(): ArrayList<RoleVO> {
        return data as ArrayList<RoleVO>
    }

}