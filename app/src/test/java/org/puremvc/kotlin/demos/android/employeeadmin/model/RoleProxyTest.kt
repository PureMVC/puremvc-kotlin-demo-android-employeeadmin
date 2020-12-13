//
//  RoleProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role

class RoleProxyTest {

    private lateinit var connection: SQLiteOpenHelper

    private lateinit var database: SQLiteDatabase

    private lateinit var cursor: SQLiteCursor

    private lateinit var roleProxy: RoleProxy

    @Before
    fun setup() {
        connection = mock(SQLiteOpenHelper::class.java)
        database = mock(SQLiteDatabase::class.java)
        cursor = mock(SQLiteCursor::class.java)
        roleProxy = RoleProxy()

        `when`(connection.readableDatabase).thenReturn(database)
        `when`(connection.writableDatabase).thenReturn(database)
        `when`(database.rawQuery(anyString(), any())).thenReturn(cursor)
        `when`(database.query(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(cursor)
        `when`(cursor.moveToNext()).thenReturn(true).thenReturn(false)
    }

    @Test
    fun testFindAll() {
        `when`(cursor.count).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("id")).thenReturn(1)
        `when`(cursor.getLong(1)).thenReturn(1)
        `when`(cursor.getColumnIndexOrThrow("name")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Administrator")

        val roles =  roleProxy.findAll()
        assertEquals(1, roles!!.size)
        assertEquals(1L, roles[0].id)
        assertEquals("Administrator", roles[0].name)
    }

    @Test
    fun testFindByUserId() {
        `when`(cursor.getColumnIndexOrThrow("id")).thenReturn(1)
        `when`(cursor.getInt(1)).thenReturn(0)
        `when`(cursor.getColumnIndexOrThrow("name")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Administrator")

        roleProxy.findByUserId(1)?.let { roles ->
            assertEquals(1, roles.size)
            assertEquals("Administrator", roles[0])
        }
    }

    @Test
    fun testUpdateRolesByUserId() {
        `when`(database.insertOrThrow(any(), any(), any())).thenReturn(1).thenReturn(2)
        `when`(cursor.count).thenReturn(1)
        val modified = roleProxy.updateByUserId(1, arrayListOf(Role(1L, "Administrator"), Role(2L, "Accounts Payable")))

        assertEquals(1, modified)
    }

}