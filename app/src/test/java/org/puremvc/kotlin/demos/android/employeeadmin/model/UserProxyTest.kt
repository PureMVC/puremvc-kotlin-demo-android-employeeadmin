//
//  UserProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@RunWith(MockitoJUnitRunner::class)
class UserProxyTest {

    private lateinit var connection: HttpURLConnection

    private lateinit var inputStream: InputStream

    private lateinit var database: SQLiteDatabase

    private lateinit var cursor: SQLiteCursor

    private lateinit var userProxy: UserProxy

    // configure: https://www.codejava.net/java-se/networking/how-to-use-java-urlconnection-and-httpurlconnection
    // https://www.codejava.net/java-se/networking/java-urlconnection-and-httpurlconnection-examples

    // https://coderwall.com/p/atzcla/mocking-urlconnection https://gist.github.com/xaethos/3792258

    // https://www.javatips.net/api/stackify-api-java-master/src/test/java/com/stackify/api/common/http/HttpClientTest.java

    // https://stackoverflow.com/questions/57108733/unit-testing-httpurlconnention

    // https://stackoverflow.com/questions/25334139/how-to-mock-a-url-connection
    // https://stackoverflow.com/questions/565535/mocking-a-url-in-java

    // pass URL to method for mocking

    @Before
    fun setup() {
        connection = mock(HttpURLConnection::class.java)
        `when`(connection.responseCode).thenReturn(200)
        val factory: (URL) -> HttpURLConnection = { connection }

        val data = "[{\"id\":1,\"first\":\"Larry\",\"last\":\"Stooge\"}]"
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)

        userProxy = UserProxy(factory)
    }

    @Test
    fun testFindAll() {
        userProxy.findAll().let { user ->
            assertEquals(1L, user!![0].id)
            assertEquals("Larry", user[0].first)
            assertEquals("Stooge", user[0].last)
        }
    }

    @Test
    fun testFindById() {
        `when`(cursor.getColumnIndexOrThrow("id")).thenReturn(1)
        `when`(cursor.getLong(1)).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("username")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("lstooge")
        `when`(cursor.getColumnIndexOrThrow("first")).thenReturn(3)
        `when`(cursor.getString(3)).thenReturn("Larry")
        `when`(cursor.getColumnIndexOrThrow("last")).thenReturn(4)
        `when`(cursor.getString(4)).thenReturn("Stooge")
        `when`(cursor.getColumnIndexOrThrow("email")).thenReturn(5)
        `when`(cursor.getString(5)).thenReturn("larry@stooges.com")
        `when`(cursor.getColumnIndexOrThrow("password")).thenReturn(6)
        `when`(cursor.getString(6)).thenReturn("ijk456")
        `when`(cursor.getColumnIndexOrThrow("departmentId")).thenReturn(7)
        `when`(cursor.getLong(7)).thenReturn(0)
        `when`(cursor.getColumnIndexOrThrow("department_name")).thenReturn(8)
        `when`(cursor.getString(8)).thenReturn("Accounting")

        userProxy.findById(1)?.let { user ->
            assertEquals(1L, user.id)
            assertEquals("lstooge", user.username)
            assertEquals("Larry", user.first)
            assertEquals("Stooge", user.last)
            assertEquals("larry@stooges.com", user.email)
            assertEquals("ijk456", user.password)
            assertEquals(0L, user.department?.id)
            assertEquals("Accounting", user.department?.name)
        }
    }

    @Test
    fun testSave() {
        `when`(database.insertOrThrow(any(), any(), any())).thenReturn(1)
        `when`(cursor.getLong(0)).thenReturn(1)
        val user = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(3, "Shipping"))

        val result = userProxy.save(user)
        assertEquals(1L, result)
    }

    @Test
    fun testUpdate() {
        `when`(database.update(any(), any(), any(), any())).thenReturn(1)
        `when`(cursor.getInt(0)).thenReturn(1)
        val user = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(3, "Shipping"))

        val result = userProxy.update(user)
        assertEquals(1, result)
    }

    @Test
    fun testDeleteById() {
        `when`(database.delete(any(), any(), any())).thenReturn(1)
        `when`(cursor.getInt(0)).thenReturn(1)

        val result = userProxy.deleteById(1)
        assertEquals(1, result)
    }

    @Test
    fun testFindAllDepartments() {
        `when`(cursor.count).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("id")).thenReturn(1)
        `when`(cursor.getLong(1)).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("name")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Accounting")

        val departments = userProxy.findAllDepartments()
        assertEquals(1, departments!!.size)
        assertEquals(1L, departments.get(0).id)
        assertEquals("Accounting", departments.get(0).name)
    }

}