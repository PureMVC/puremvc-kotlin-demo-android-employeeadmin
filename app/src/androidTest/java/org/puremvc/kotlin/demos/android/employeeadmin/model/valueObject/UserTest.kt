package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import junit.framework.TestCase
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R

class UserTest : TestCase() {

    fun testValidate() {
        val user = User()

        assertEquals(Application.context?.resources?.getString(R.string.error_invalid_data), user.validate(""))
    }

    fun testValidateConfirmPassword() {
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping"))

        assertEquals(Application.context?.resources?.getString(R.string.error_password), joe.validate("abc"))
        assertNull(null, joe.validate("abc123"))
    }

}