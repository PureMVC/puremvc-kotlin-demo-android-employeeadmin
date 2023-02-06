//
//  RoleProxyTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.puremvc.kotlin.demos.android.employeeadmin.controller.AppDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.data.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

@RunWith(MockitoJUnitRunner::class)
class RoleProxyTest {

    private lateinit var database: AppDatabase

    private lateinit var roleDAO: RoleDAO

    @Before
    fun setup() {
        database = mock(AppDatabase::class.java)
        `when`(mock(Room::class.java).inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .build()).thenReturn(database)
        `when`(database.roleDAO()).thenReturn(mock(RoleDAO::class.java))

        roleDAO = database.roleDAO()
    }

    @Test
    fun testFindAll() {
        `when`(roleDAO.findAll()).thenReturn(listOf(Role(1, "Administrator")))

        val roles =  roleDAO.findAll()
        assertEquals(1, roles.size)

        assertEquals(1L, roles[0].id)
        assertEquals("Administrator", roles[0].name)
    }

    @Test
    fun testFindByUserId() {
        `when`(roleDAO.findByUserId(any())).thenReturn(listOf(Role(1, "Administrator")))

        roleDAO.findByUserId(1).let {
            assertEquals(1, it.size)
            assertEquals("Administrator", it[0].name)
        }
    }

    @Test
    fun testUpdateRolesByUserId() {
        `when`(roleDAO.updateByUserId(any(), any())).thenReturn(Unit)

        val modified = roleDAO.updateByUserId(1, listOf(UserRoleJoin(1, 1), UserRoleJoin(1, 2)))

        // assertEquals(1, modified)
    }

}