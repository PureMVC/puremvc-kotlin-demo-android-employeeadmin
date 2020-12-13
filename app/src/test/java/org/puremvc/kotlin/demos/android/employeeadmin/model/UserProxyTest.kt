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
import android.database.sqlite.SQLiteOpenHelper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

@RunWith(MockitoJUnitRunner::class)
class UserProxyTest {

    private lateinit var connection: SQLiteOpenHelper

    private lateinit var database: SQLiteDatabase

    private lateinit var cursor: SQLiteCursor

    private lateinit var userProxy: UserProxy

    @Before
    fun setup() {
        connection = mock(SQLiteOpenHelper::class.java)
        database = mock(SQLiteDatabase::class.java)
        cursor = mock(SQLiteCursor::class.java)
        userProxy = UserProxy()

        `when`(connection.readableDatabase).thenReturn(database)
        `when`(connection.writableDatabase).thenReturn(database)
        `when`(database.rawQuery(anyString(), any())).thenReturn(cursor)
        `when`(database.query(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(cursor)
        `when`(cursor.moveToNext()).thenReturn(true).thenReturn(false)
        `when`(cursor.moveToFirst()).thenReturn(true)
    }

    @Test
    fun testFindAll() {
        `when`(cursor.count).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("id")).thenReturn(1)
        `when`(cursor.getLong(1)).thenReturn(1)

        `when`(cursor.getColumnIndexOrThrow("first")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Larry")

        `when`(cursor.getColumnIndexOrThrow("last")).thenReturn(3)
        `when`(cursor.getString(3)).thenReturn("Stooge")

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