//
//  UserProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

class UserProxyTest {

    @Test
    fun addItem() {
        val userProxy = UserProxy()
        userProxy.addItem(UserVO("jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", DeptEnum.SHIPPING))

        assertEquals(1, userProxy.users.size)
        val userVO = userProxy.users[0]
        assertEquals(userVO.username, "jstooge")
        assertEquals(userVO.first, "Joe")
        assertEquals(userVO.last, "Stooge")
        assertEquals(userVO.email, "joe@stooges.com")
        assertEquals(userVO.password, "abc123")
        assertEquals(userVO.department, DeptEnum.SHIPPING)
    }

    @Test
    fun updateItem() {
        val userProxy = UserProxy()
        userProxy.addItem(UserVO("jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", DeptEnum.SHIPPING))

        userProxy.updateItem(UserVO("jstooge", "Joe1", "Stooge1", "joe1@stooges.com", "xyz987", DeptEnum.QC))
        val userVO = userProxy.users[0]
        assertEquals(userVO.username, "jstooge")
        assertEquals(userVO.first, "Joe1")
        assertEquals(userVO.last, "Stooge1")
        assertEquals(userVO.email, "joe1@stooges.com")
        assertEquals(userVO.password, "xyz987")
        assertEquals(userVO.department, DeptEnum.QC)
    }

    @Test
    fun deleteItem() {
        val userProxy = UserProxy()
        userProxy.addItem(UserVO("jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", DeptEnum.SHIPPING))
        userProxy.addItem(UserVO("sstooge", "Shemp", "Stooge", "shemp@stooges.com", "xyz983", DeptEnum.QC))

        userProxy.deleteItem("sstooge")
        assertEquals(1, userProxy.users.size)
    }

}