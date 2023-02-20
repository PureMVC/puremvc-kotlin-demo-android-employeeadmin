//
//  UserTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain.entity

import junit.framework.TestCase
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User

class UserTest : TestCase() {

    fun testValidate() {
        val joe = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 4)

        assertEquals("Your password and confirmation password do not match.", joe.validate(""))
    }

    fun testValidateConfirmPassword() {
        val joe = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 4)

        assertEquals("Invalid Form Data.", joe.validate("abc"))
        assertNull(null, joe.validate("abc123"))
    }

}