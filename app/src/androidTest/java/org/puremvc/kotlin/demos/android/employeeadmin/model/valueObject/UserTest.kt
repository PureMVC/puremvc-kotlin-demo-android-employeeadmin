package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import junit.framework.TestCase

class UserTest : TestCase() {

    fun testValidate() {
        val user = User()

        assertEquals("Your password and confirmation password do not match.", user.validate(""))
    }

    fun testValidateConfirmPassword() {
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping"))

        assertEquals("Invalid Form Data.", joe.validate("abc"))
        assertNull(null, joe.validate("abc123"))
    }

}