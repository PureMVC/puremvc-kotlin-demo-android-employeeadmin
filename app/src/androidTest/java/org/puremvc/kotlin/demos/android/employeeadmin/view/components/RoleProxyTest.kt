//
//  RoleProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import java.net.HttpURLConnection
import java.net.URL

class RoleProxyTest {

    lateinit var userProxy: UserProxy

    lateinit var roleProxy: RoleProxy

    @Before
    fun setup() {
        val factory: (URL) -> HttpURLConnection = { url ->
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 2500
            connection.connectTimeout = 2500
            connection
        }

        userProxy = UserProxy(factory)
        roleProxy = RoleProxy(factory)
    }

    @Test
    fun testFindAll() {
        val roles = roleProxy.findAll()
        assertEquals(14, roles!!.size)
        roles.forEach { role ->
            assertNotNull(role.id)
            assertNotNull(role.name)
        }
    }

    @Test
    fun testFindAllByUserId() {
        val roles = roleProxy.findByUserId(3)!!
        assertEquals(3, roles.size)
        roles.forEach { role ->
            assertNotNull(role.id)
            assertNotNull(role.name)
        }
    }

    @Test
    fun testUpdateAndFindById() {
        roleProxy.updateByUserId(1, listOf(Role(1, "Administrator"), Role(2, "Accounts Payable")))
        val roles = roleProxy.findByUserId(1)!!
        assertEquals(2, roles.size)

        roleProxy.updateByUserId(1, listOf(Role(4, "Employee Benefits")))

        val roles2 = roleProxy.findByUserId(1)!!
        assertEquals(1, roles2.size)
    }

}