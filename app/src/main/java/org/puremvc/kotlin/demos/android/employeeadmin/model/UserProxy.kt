//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import androidx.lifecycle.LiveData
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.UserDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserDepartment
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy(private val userDAO: UserDAO): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

//    fun findAll(): List<User> {
//        return userDAO.findAll()
//    }

//    fun findAll2(): LiveData<User> {
//        return userDAO.findAll2()
//    }
//
    fun findById(id: Long): UserDepartment {
        return userDAO.findById(id)
    }
//
//    fun save(user: User): Long {
//        return userDAO.save(user)
//    }
//
//    fun update(user: User): Int {
//        return userDAO.update(user)
//    }
//
//    fun deleteById(id: Long): Int {
//        return userDAO.deleteById(id)
//    }
//
//    fun findAllDepartments(): List<Department> {
//        return userDAO.findAllDepartments()
//    }

}