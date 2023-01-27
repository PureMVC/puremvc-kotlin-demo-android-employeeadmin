package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.R

class UserTest : TestCase() {

    lateinit var context: Context

    @Before
    override fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testValidate() {
        val user = User()

        assertEquals(context.resources?.getString(R.string.error_invalid_data), user.validate(""))
    }

    @Test
    fun testValidateConfirmPassword() {
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping"))

        assertEquals(context.resources?.getString(R.string.error_password), joe.validate("abc"))
        assertNull(null, joe.validate("abc123"))
    }

}