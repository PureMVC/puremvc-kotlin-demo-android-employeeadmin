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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role

class RoleProxyTest {

    lateinit var connection: SQLiteOpenHelper

    lateinit var userProxy: UserProxy

    lateinit var roleProxy: RoleProxy

    @Before
    fun setup() {
        connection = object: SQLiteOpenHelper(InstrumentationRegistry.getInstrumentation().targetContext, "employeeadmin_test.db", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL("PRAGMA foreign_keys = ON")

                db?.execSQL("CREATE TABLE department(id INTEGER PRIMARY KEY, name TEXT NOT NULL)")
                db?.execSQL("INSERT INTO department(id, name) VALUES(1, 'Accounting'), (2, 'Sales'), (3, 'Plant'), (4, 'Shipping'), (5, 'Quality Control')")

                db?.execSQL("CREATE TABLE role(id INTEGER PRIMARY KEY, name TEXT NOT NULL)")
                db?.execSQL("INSERT INTO role(id, name) VALUES(1, 'Administrator'), (2, 'Accounts Payable'), (3, 'Accounts Receivable'), (4, 'Employee Benefits'), (5, 'General Ledger'),(6, 'Payroll'), (7, 'Inventory'), (8, 'Production'), (9, 'Quality Control'), (10, 'Sales'), (11, 'Orders'), (12, 'Customers'), (13, 'Shipping'), (14, 'Returns')")

                db?.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY, username TEXT NOT NULL UNIQUE, first TEXT NOT NULL, last TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, department_id INTEGER NOT NULL, FOREIGN KEY(department_id) REFERENCES department(id) ON DELETE CASCADE ON UPDATE NO ACTION)")
                db?.execSQL("INSERT INTO user(id, username, first, last, email, password, department_id) VALUES(1, 'lstooge', 'Larry', 'Stooge', 'larry@stooges.com', 'ijk456', 1), (2, 'cstooge', 'Curly', 'Stooge', 'curly@stooges.com', 'xyz987', 2), (3, 'mstooge', 'Moe', 'Stooge', 'moe@stooges.com', 'abc123', 3)")

                db?.execSQL("CREATE TABLE user_role(user_id INTEGER NOT NULL, role_id INTEGER NOT NULL, PRIMARY KEY(user_id, role_id), FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY(role_id) REFERENCES role(id) ON DELETE CASCADE ON UPDATE NO ACTION)")
                db?.execSQL("INSERT INTO user_role(user_id, role_id) VALUES(1, 4), (2, 3), (2, 5), (3, 8), (3, 10), (3, 13)")
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
        }

        connection.readableDatabase.use {
            userProxy = UserProxy(connection)
        }
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