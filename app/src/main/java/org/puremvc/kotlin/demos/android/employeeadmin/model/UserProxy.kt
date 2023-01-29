//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy: Proxy(NAME, ArrayList<User>()) {

    companion object {
        const val NAME = "UserProxy"
    }

    fun findByUsername(username: String): User? {
        for (i in 0 until users.size) {
            if (users[i].username == username) {
                return users[i]
            }
        }
        return null
    }

    fun addItem(userVO: User) {
        users.add(userVO)
    }

    fun updateItem(userVO: User) {
        for (i in 0 until users.size) {
            if (users[i].username == userVO.username) {
                users[i] = userVO
                break
            }
        }
    }

    fun deleteItem(username: String) {
        for (i in 0 until users.size) {
            if (users[i].username == username) {
                users.removeAt(i)
                break
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    val users: ArrayList<User> get() = data as ArrayList<User>

}