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
        users().add(userVO)
    }

    fun updateItem(userVO: UserVO) {
        val list = users()
        for (i in 0 until list.size) {
            if (list[i].username == userVO.username) {
                list[i] = userVO
            }
        }
    }

    fun deleteItem(userVO: UserVO) {
        val list = users()
        for (i in 0 until list.size) {
            if (list[i].username == userVO.username) {
                list.removeAt(i)
            }
        }
    }

    fun users() : ArrayList<UserVO> {
        return data as ArrayList<UserVO>
    }

}