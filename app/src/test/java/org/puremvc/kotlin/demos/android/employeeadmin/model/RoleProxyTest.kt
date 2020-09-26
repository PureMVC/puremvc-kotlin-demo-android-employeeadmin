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
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*

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
        roleProxy = RoleProxy(connection)

        `when`(connection.readableDatabase).thenReturn(database)
        `when`(connection.writableDatabase).thenReturn(database)
        `when`(database.rawQuery(anyString(), any())).thenReturn(cursor)
        `when`(database.query(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(cursor)
        `when`(cursor.moveToNext()).thenReturn(true).thenReturn(false)
    }

    @Test
    fun testFindAll() {
        `when`(cursor.count).thenReturn(1)
        `when`(cursor.getColumnIndex("id")).thenReturn(1)
        `when`(cursor.getInt(1)).thenReturn(1)
        `when`(cursor.getColumnIndex("name")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Administrator")

        runBlocking {
            val roles =  roleProxy.findAll()
            assertEquals(1, roles!!.size)
            assertEquals("Administrator", roles[0])
        }
    }

    @Test
    fun testFindAllByUserId() {
        `when`(cursor.getColumnIndex("id")).thenReturn(1)
        `when`(cursor.getInt(1)).thenReturn(0)
        `when`(cursor.getColumnIndex("name")).thenReturn(2)
        `when`(cursor.getString(2)).thenReturn("Administrator")

        runBlocking {
            roleProxy.findAllByUserId(1)?.let { roles ->
                assertEquals(1, roles.size)
                assertEquals("Administrator", roles[0])
            }
        }
    }

    @Test
    fun testUpdateRolesByUserId() {
        `when`(database.insertOrThrow(any(), any(), any())).thenReturn(1).thenReturn(2)
        runBlocking {
            val ids = roleProxy.updateRolesByUserId(1, hashMapOf(1L to "Administrator", 2L to "Accounts Payable"))

            assertEquals(2, ids!!.size)
            assertEquals(1, ids[0])
            assertEquals(2, ids[1])
        }
    }

}