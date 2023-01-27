package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import junit.framework.TestCase
import org.junit.Test

class DepartmentTest : TestCase() {

    @Test
    fun testValidate() {
        val department = Department(0, "None")
        assertFalse(department.validate())
    }

    @Test
    fun testValidateValid() {
        val department = Department(1, "Accounting")
        assertTrue(department.validate())
    }

}