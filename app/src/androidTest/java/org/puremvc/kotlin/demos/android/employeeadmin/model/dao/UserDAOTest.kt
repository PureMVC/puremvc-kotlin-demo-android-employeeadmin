//
//  UserDAOTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.puremvc.kotlin.demos.android.employeeadmin.controller.AppDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

@RunWith(AndroidJUnit4::class)
class UserDAOTest: TestCase() {

    lateinit var database: AppDatabase

    lateinit var userDAO: UserDAO

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        userDAO = database.userDAO()

        database.userDAO().insertAll(listOf(Department(1, "Accounting"),
            Department(2, "Sales"), Department(3, "Plant"),
            Department(4, "Shipping"), Department(5, "Quality Control")))

        database.userDAO().save(User(1, "lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", 3))
        database.userDAO().save(User(2, "cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", 4))
        database.userDAO().save(User(3, "mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", 5))
    }

    @Test
    fun testFindAll() {
        val users = userDAO.findAll()
        assertEquals(3, users.size)
        users.forEach {
            assertNotNull(it.id)
            assertNotNull(it.first)
            assertNotNull(it.last)
        }
    }

    @Test
    fun testFindById() {
        val user = userDAO.findById(1).keys.first()
        assertNotNull(user)
        assertEquals(user.id, 1L)
        assertEquals(user.first,  "Larry")
        assertEquals(user.last,  "Stooge")
    }

    @Test
    fun testSaveAndFindByIdAndDelete() {
        val joe = User(0, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 4)
        val id = userDAO.save(joe)
        val map = userDAO.findById(id)
        val user = map.keys.first()
        val department = map.values.first()

        assertNotNull(user.id)
        assertNotNull(user.username)
        assertNotNull(user.first)
        assertNotNull(user.last)
        assertNotNull(user.email)
        assertNotNull(user.password)
        assertNotNull(department)
        assertNotNull(department.id)
        assertNotNull(department.name)

        userDAO.deleteById(id)
        assertNull(userDAO.findById(id))
    }

    @Test
    fun testUpdate() { // delete manually to reset state on failure
        val joe = User(0, "jstooge", "Joe", "Stooge", "joe@stooges.com", "abc123", 4) // insert new
        val id = userDAO.save(joe)

        userDAO.update(User(id, "jstooge", "Joe1", "Stooge1", "joe1@stooges.com", "abc123", 5))

        val map = userDAO.findById(id)
        val user = map.keys.first()
        val department = map.values.first()

        assertEquals(id, user.id)
        assertEquals("jstooge", user.username)
        assertEquals("Joe1", user.first)
        assertEquals("Stooge1", user.last)
        assertEquals("joe1@stooges.com", user.email)
        assertEquals("abc123", user.password)
        assertEquals(5L, department.id)
        assertEquals("Quality Control", department.name)

        userDAO.deleteById(id)
    }

    @Test
    fun testFindAllDepartments() {
        val departments = userDAO.findAllDepartments()
        assertEquals(5, departments.size)
        departments.forEach {
            assertNotNull(it.id)
            assertNotNull(it.name)
        }
    }

    @After
    fun teardown() {
        database.close()
    }

}