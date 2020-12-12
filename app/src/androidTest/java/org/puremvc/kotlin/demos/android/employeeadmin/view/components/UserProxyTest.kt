//
//  UserProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

class UserProxyTest {

    lateinit var connection: SQLiteOpenHelper

    lateinit var userProxy: UserProxy

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

        connection.readableDatabase.use { database ->
            userProxy = UserProxy(connection)
        }
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
    fun testSaveAndFindByIdAndDelete() {
        val joe = User(0, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping"))
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
        assertNull(userProxy.findById(id))
    }

    @Test
    fun testUpdate() { // delete manually to reset state on failure
        val joe = User(null, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Department(4, "Shipping")) // insert new
        val id = userProxy.save(joe)

        userProxy.update(User(id, "jstooge", "Joe1", "Stooge1", "joe1@stooges.com", "abc123", Department(5, "Quality Control")))

        val user = userProxy.findById(id!!)!!

        assertEquals(id, user.id)
        assertEquals("jstooge", user.username)
        assertEquals("Joe1", user.first)
        assertEquals("Stooge1", user.last)
        assertEquals("joe1@stooges.com", user.email)
        assertEquals("abc123", user.password)
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