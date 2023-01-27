//
//  UserProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class UserProxyTest {

    private lateinit var connection: HttpURLConnection

    private lateinit var inputStream: InputStream

    private lateinit var userProxy: UserProxy

    @Before
    fun setup() {
        connection = mock(HttpURLConnection::class.java)
        `when`(connection.responseCode).thenReturn(200)
        val factory: (URL) -> HttpURLConnection = { connection }
        val outputStream = mock(OutputStream::class.java)
        `when`(connection.outputStream).thenReturn(outputStream)

        userProxy = UserProxy(factory)
    }

    @Test
    fun testFindAll() {
        val data = """[{"id":1,"first":"Larry","last":"Stooge"}]"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)

        userProxy.findAll().let { users ->
            assertNotNull(users)
            assertTrue(users!!.size > 0)
            assertEquals(1L, users!![0].id)
            assertEquals("Larry", users[0].first)
            assertEquals("Stooge", users[0].last)
        }
    }

    @Test
    fun testFindById() {
        val data = """{"id":1, "username": "lstooge", "first":"Larry","last":"Stooge", "email": "larry@stooges.com", "password": "ijk456", "department": {"id": 1, "name": "Accounting"}}"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)

        userProxy.findById(1)?.let { user ->
            assertEquals(1L, user.id)
            assertEquals("lstooge", user.username)
            assertEquals("Larry", user.first)
            assertEquals("Stooge", user.last)
            assertEquals("larry@stooges.com", user.email)
            assertEquals("ijk456", user.password)
            assertEquals(1L, user.department?.id)
            assertEquals("Accounting", user.department?.name)
        }
    }

    @Test
    fun testSave() {
        val data = """{"id":1, "username": "jstooge", "first":"Joe", "last":"Stooge", "email": "joe@stooges.com", "password": "abc123", "department": {"id": 3, "name": "Shipping"}}"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(201)

        val user = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(3, "Shipping"))
        val result = userProxy.save(user)
        assertEquals(1L, result)
    }

    @Test
    fun testUpdate() {
        val data = """{"id":1, "username": "jstooge", "first":"Joe", "last":"Stooge", "email": "joe@stooges.com", "password": "abc123", "department": {"id": 3, "name": "Shipping"}}"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)
        val user = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(3, "Shipping"))

        val result = userProxy.update(user)
        assertEquals(1, result)
    }

    @Test
    fun testDeleteById() {
        `when`(connection.responseCode).thenReturn(204)
        val result = userProxy.deleteById(1)
        assertEquals(1, result)
    }

    @Test
    fun testFindAllDepartments() {
        val data = """[{"id":1, "name": "Accounting"}]"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)
        val departments = userProxy.findAllDepartments()

        assertEquals(1, departments!!.size)
        assertEquals(1L, departments.get(0).id)
        assertEquals("Accounting", departments.get(0).name)
    }

}