//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy: Proxy(NAME, ArrayList<UserVO>()) {

    companion object {
        const val NAME = "UserProxy"
    }

    fun addItem(userVO: UserVO) {
        users.add(userVO)
    }

    fun updateItem(userVO: UserVO) {
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
    val users: ArrayList<UserVO> get() = data as ArrayList<UserVO>

}