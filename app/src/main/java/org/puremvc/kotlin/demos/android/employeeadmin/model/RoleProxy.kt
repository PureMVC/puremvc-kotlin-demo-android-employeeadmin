//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class RoleProxy(private val roleDAO: RoleDAO): Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    fun findAll(): List<Role> {
        return roleDAO.findAll()
    }

    fun findByUserId(id: Long): List<Role> {
        return roleDAO.findByUserId(id)
    }

//    fun deleteById(id: Long) {
//        return roleDAO.deleteById(id)
//    }
//
//    fun updateByUserId(id: Long, role: Role) {
//        return roleDAO.updateByUserId(id, role)
//    }

}
