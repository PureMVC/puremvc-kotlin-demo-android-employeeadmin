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
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO

class UserProxyTest {

    lateinit var connection: SQLiteOpenHelper

    lateinit var userProxy: UserProxy

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
            userProxy = UserProxy(connection)
        }
    }

    @Test
    fun testFindAll() {
        runBlocking {
            val users = userProxy.findAll()
            assertEquals(3, users!!.size)
            users.forEach { user ->
                assertNotNull(user.id)
                assertNotNull(user.first)
                assertNotNull(user.last)
            }
        }
    }

    @Test
    fun testSaveAndFindByIdAndDelete() {
        runBlocking {
            val joe = UserVO(0, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Pair(4, "Shipping"))
            val id = userProxy.save(joe)
            val user = userProxy.findById(id)!!

            assertNotNull(user.id)
            assertNotNull(user.username)
            assertNotNull(user.first)
            assertNotNull(user.last)
            assertNotNull(user.email)
            assertNotNull(user.password)
            assertNotNull(user.department)
            assertNotNull(user.department!!.first)
            assertNotNull(user.department!!.second)

            userProxy.deleteById(id)
            assertNull(userProxy.findById(id))
        }
    }

    @Test
    fun testUpdate() { // delete manually to reset state on failure
        runBlocking {
            val joe = UserVO(0, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", Pair(4, "Shipping")) // insert new
            val id = userProxy.save(joe)

            userProxy.update(UserVO(id, "jstooge", "Joe1", "Stooge1", "joe1@stooges.com", "abc123", Pair(5, "Quality Control")))

            val user = userProxy.findById(id)!!

            assertEquals(id, user.id)
            assertEquals("jstooge", user.username)
            assertEquals("Joe1", user.first)
            assertEquals("Stooge1", user.last)
            assertEquals("joe1@stooges.com", user.email)
            assertEquals("abc123", user.password)
            assertEquals(Pair(5L, "Quality Control"), user.department)
            assertEquals(5L, user.department!!.first)
            assertEquals("Quality Control", user.department!!.second)

            userProxy.deleteById(id) // revert
        }
    }

    @Test
    fun testFindAllDepartments() {
        runBlocking {
            val departments = userProxy.findAllDepartments()
            assertEquals(5, departments!!.size)
            departments.forEach { department ->
                assertNotNull(department.key)
                assertNotNull(department.value)
            }
        }
    }

}