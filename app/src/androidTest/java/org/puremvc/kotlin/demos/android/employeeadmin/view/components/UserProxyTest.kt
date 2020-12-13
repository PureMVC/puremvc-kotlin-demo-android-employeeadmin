//
//  UserProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.Exception

class UserProxyTest {

    lateinit var userProxy: UserProxy

    @Before
    fun setup() {
        userProxy = UserProxy()
    }

    @After
    fun teardown() {
    }

    @Test
    fun testFindAll() {
        val users = userProxy.findAll()
        assertEquals(3, users!!.size)
        users.forEach { user ->
            assertNotNull(user.id)
            assertNotNull(user.first)
            assertNotNull(user.last)
        }
    }

    @Test
    fun testFindById() {
        val user = userProxy.findById(1)
        assertNotNull(user)
        assertEquals(user!!.id, 1L)
        assertEquals(user.first,  "Larry")
        assertEquals(user.last,  "Stooge")

        print(user.toJSONObject())
    }

    @Test
    fun testSaveAndFindByIdAndDelete() {
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping"))
        val id = userProxy.save(joe)
        val user = userProxy.findById(id!!)!!

        assertNotNull(user.id)
        assertNotNull(user.username)
        assertNotNull(user.first)
        assertNotNull(user.last)
        assertNotNull(user.email)
        assertNotNull(user.password)
        assertNotNull(user.department)
        assertNotNull(user.department!!.id)
        assertNotNull(user.department!!.name)

        userProxy.deleteById(id)
        try {
            userProxy.findById(id)
            fail("Should have thrown an exception");
        } catch (exception: Exception) {
        }
    }

    @Test
    fun testUpdate() {
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping")) // insert new
        val id = userProxy.save(joe)

        userProxy.update(User(id, "jstooge", "Joe1", "Stooge1", "joe1@stooges.com", "abc123", Department(5, "Quality Control")))

        val user = userProxy.findById(id!!)!!

        assertEquals(id, user.id)
        assertEquals("jstooge", user.username)
        assertEquals("Joe1", user.first)
        assertEquals("Stooge1", user.last)
        assertEquals("joe1@stooges.com", user.email)
        assertEquals(5L, user.department!!.id)
        assertEquals("Quality Control", user.department!!.name)

        userProxy.deleteById(id) // revert
    }

    @Test
    fun testFindAllDepartments() {
        val departments = userProxy.findAllDepartments()
        assertEquals(5, departments!!.size)
        departments.forEach { department ->
            assertNotNull(department.id)
            assertNotNull(department.name)
        }
    }

}