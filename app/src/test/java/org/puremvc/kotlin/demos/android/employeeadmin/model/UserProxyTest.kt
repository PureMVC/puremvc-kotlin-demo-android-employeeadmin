//
//  UserProxyTest.kt
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
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.UserDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

@RunWith(MockitoJUnitRunner::class)
class UserProxyTest {

    private lateinit var database: AppDatabase

    private lateinit var userDAO: UserDAO

    @Before
    fun setup() {
        database = mock(AppDatabase::class.java)
        `when`(mock(Room::class.java).inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .build()).thenReturn(database)
        `when`(database.roleDAO()).thenReturn(mock(RoleDAO::class.java))

        userDAO = database.userDAO()


    }

    @Test
    fun testFindAll() {
        userDAO.insertAll(listOf(Department(1, "Accounting"),
            Department(2, "Sales"), Department(3, "Plant"),
            Department(4, "Shipping"), Department(5, "Quality Control")))

        database.userDAO().save(User(1, "lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", 3))
        database.userDAO().save(User(2, "cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", 4))
        database.userDAO().save(User(3, "mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", 5))

        userDAO.findAll().let {
            assertEquals(1L, it[0].id)
            assertEquals("Larry", it[0].first)
            assertEquals("Stooge", it[0].last)
        }
    }

    @Test
    fun testFindById() {
        userDAO.insertAll(listOf(Department(1, "Accounting"),
            Department(2, "Sales"), Department(3, "Plant"),
            Department(4, "Shipping"), Department(5, "Quality Control")))

        database.userDAO().save(User(1, "lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", 3))
        database.userDAO().save(User(2, "cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", 4))
        database.userDAO().save(User(3, "mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", 5))

        val map = userDAO.findById(1)
        val user = map.keys.first()
        val department = map.values.first()

        assertEquals(1L, user.id)
        assertEquals("lstooge", user.username)
        assertEquals("Larry", user.first)
        assertEquals("Stooge", user.last)
        assertEquals("larry@stooges.com", user.email)
        assertEquals("ijk456", user.password)
        assertEquals(0L, department.id)
        assertEquals("Accounting", department.name)
    }

    @Test
    fun testSave() {
        `when`(userDAO.save(any())).thenReturn(1)
        val user = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 3)

        val result = userDAO.save(user)
        assertEquals(1L, result)
    }

    @Test
    fun testUpdate() {
        `when`(userDAO.update(any())).thenReturn(1)
        val user = User(1, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 3)

        val result = userDAO.update(user)
        assertEquals(1, result)
    }

    @Test
    fun testDeleteById() {
        `when`(userDAO.deleteById(any())).thenReturn(1)

        val result = userDAO.deleteById(1)
        assertEquals(1, result)
    }

    @Test
    fun testFindAllDepartments() {
        `when`(userDAO.findAllDepartments()).thenReturn(listOf(Department(1, "Accounting")))

        val departments = userDAO.findAllDepartments()
        assertEquals(1, departments.size)
        assertEquals(1L, departments[0].id)
        assertEquals("Accounting", departments[0].name)
    }

}