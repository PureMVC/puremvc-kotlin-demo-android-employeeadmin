package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import junit.framework.TestCase

class DepartmentTest : TestCase() {

    fun testValidate() {
        val department = Department(0, "None")
        assertFalse(department.validate())
    }

    fun testValidateValid() {
        val department = Department(1, "Accounting")
        assertTrue(department.validate())
    }

}