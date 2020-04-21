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
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO

class RoleProxyTest {

    @Test
    fun testAddItem() {
        val roleProxy = RoleProxy()
        roleProxy.addItem(RoleVO("jstooge", arrayListOf(RoleEnum.SALES, RoleEnum.RETURNS, RoleEnum.SHIPPING)))

        assertEquals(1, roleProxy.roles.size)
    }

    @Test
    fun testGetRoleVO() {
        val roleProxy = RoleProxy()
        roleProxy.addItem(RoleVO("jstooge", arrayListOf(RoleEnum.SALES, RoleEnum.RETURNS, RoleEnum.SHIPPING)))

        val roles = roleProxy.getUserRoles("jstooge")!!
        assertEquals(3, roles.size)
        assertEquals(RoleEnum.SALES, roles[0])
        assertEquals(RoleEnum.RETURNS, roles[1])
        assertEquals(RoleEnum.SHIPPING, roles[2])
    }

    @Test
    fun testUpdateUserRoles() {
        val roleProxy = RoleProxy()
        roleProxy.addItem(RoleVO("jstooge", arrayListOf(RoleEnum.SALES, RoleEnum.RETURNS, RoleEnum.SHIPPING)))
        roleProxy.addItem(RoleVO("sstooge", arrayListOf(RoleEnum.ADMIN, RoleEnum.ACCT_PAY)))

        roleProxy.updateUserRoles("sstooge", arrayListOf(RoleEnum.RETURNS))
        val roles = roleProxy.getUserRoles("sstooge")!!
        assertEquals(1, roles.size)
        assertEquals(RoleEnum.RETURNS, roles[0])
    }

    @Test
    fun testDeleteItem() {
        val roleProxy = RoleProxy()
        roleProxy.addItem(RoleVO("jstooge", arrayListOf(RoleEnum.SALES, RoleEnum.RETURNS, RoleEnum.SHIPPING)))
        roleProxy.addItem(RoleVO("sstooge", arrayListOf(RoleEnum.ADMIN, RoleEnum.ACCT_PAY)))

        roleProxy.deleteItem("sstooge")
        assertNull(roleProxy.getUserRoles("sstooge"))
        assertEquals(1, roleProxy.roles.size)

        roleProxy.deleteItem("jstooge")
        assertNull(roleProxy.getUserRoles("jstooge"))
        assertEquals(0, roleProxy.roles.size)
    }
}