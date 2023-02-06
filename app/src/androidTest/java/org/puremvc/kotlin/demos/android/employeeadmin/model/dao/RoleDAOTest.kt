//
//  RoleDAOTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.puremvc.kotlin.demos.android.employeeadmin.controller.AppDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.data.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

@RunWith(AndroidJUnit4::class)
@LargeTest
class RoleDAOTest: TestCase() {

    lateinit var database: AppDatabase

    lateinit var roleDAO: RoleDAO

    @Before
    override fun setUp() {
        super.setUp()
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        roleDAO = database.roleDAO()
    }

    @Test
    fun testFindAll() {
        val roles = roleDAO.findAll()
        assertEquals(14, roles.size)
        roles.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
        }
    }

    @Test
    fun testFindAllByUserId() {
        val roles = roleDAO.findByUserId(3)
        assertEquals(3, roles.size)
        roles.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
        }
    }

    @Test
    fun testUpdateAndFindById() {
        roleDAO.updateByUserId(1, listOf(UserRoleJoin(1, 1), UserRoleJoin(1, 2)))

        val roles = roleDAO.findByUserId(1)
        assertEquals(2, roles.size)

        roleDAO.updateByUserId(1, listOf(UserRoleJoin(1, 4)))

        val roles2 = roleDAO.findByUserId(1)
        assertEquals(1, roles2.size)
    }

    @After
    override fun tearDown() {
        database.close()
        super.tearDown()
    }

}