//
//  RoleProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy

class RoleProxyTest {

    lateinit var connection: SQLiteOpenHelper

    lateinit var roleProxy: RoleProxy

    @Before
    fun setup() {
        connection = object: SQLiteOpenHelper(InstrumentationRegistry.getInstrumentation().targetContext, "employeeadmin.db", null, 1) {
            override fun onConfigure(db: SQLiteDatabase?) {
                super.onConfigure(db)
                db?.setForeignKeyConstraintsEnabled(true)
            }

            override fun onCreate(db: SQLiteDatabase?) {

            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
        }

        connection.readableDatabase.use { database ->
            roleProxy = RoleProxy(connection)
        }
    }

    @Test
    fun testFindAll() {
        runBlocking {
            val roles = roleProxy.findAll()
            assertEquals(14, roles!!.size)
            roles.forEach { role ->
                assertNotNull(role.key)
                assertNotNull(role.value)
            }
        }
    }

    @Test
    fun testFindAllByUserId() {
        runBlocking {
            val roles = roleProxy.findAllByUserId(3)!!
            assertEquals(3, roles.size)
            roles.forEach { role ->
                assertNotNull(role.key)
                assertNotNull(role.value)
            }
        }
    }

    @Test
    fun testUpdateAndFindById() {
        runBlocking {
            roleProxy.updateRolesByUserId(1, hashMapOf(1L to "Administrator", 2L to "Accounts Payable"))
            val roles = roleProxy.findAllByUserId(1)!!
            assertEquals(2, roles.size)

            roleProxy.updateRolesByUserId(1, hashMapOf(4L to "Employee Benefits"))

            val roles2 = roleProxy.findAllByUserId(1)!!
            assertEquals(1, roles2.size)
        }
    }

}