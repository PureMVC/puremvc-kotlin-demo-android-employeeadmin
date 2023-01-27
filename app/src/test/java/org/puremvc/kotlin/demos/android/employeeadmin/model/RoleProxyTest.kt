//
//  RoleProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class RoleProxyTest {

    private lateinit var connection: HttpURLConnection

    private lateinit var inputStream: InputStream

    private lateinit var roleProxy: RoleProxy

    @Before
    fun setup() {
        connection = mock(HttpURLConnection::class.java)
        `when`(connection.responseCode).thenReturn(200)
        val factory: (URL) -> HttpURLConnection = { connection }
        val outputStream = mock(OutputStream::class.java)
        `when`(connection.outputStream).thenReturn(outputStream)

        roleProxy = RoleProxy(factory)
    }

    @Test
    fun testFindAll() {
        val data = """[{"id":1,"name":"Administrator"}]"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)

        val roles =  roleProxy.findAll()
        assertEquals(1, roles.size)
        assertEquals(1L, roles[0].id)
        assertEquals("Administrator", roles[0].name)
    }

    @Test
    fun testFindByUserId() {
        val data = """[{"id":1,"name":"Administrator"}]"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)

        roleProxy.findByUserId(1).let { roles ->
            assertEquals(1, roles.size)
            assertEquals("Administrator", roles[0].name)
        }
    }

    @Test
    fun testUpdateRolesByUserId() {
        val data = """[4, 5, 6]"""
        inputStream = data.byteInputStream()
        `when`(connection.inputStream).thenReturn(inputStream)
        `when`(connection.responseCode).thenReturn(200)

        val modified = roleProxy.updateByUserId(1, arrayListOf(Role(1L, "Administrator"), Role(2L, "Accounts Payable")))

        assertEquals(1, modified)
    }

}