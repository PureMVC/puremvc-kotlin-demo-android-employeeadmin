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
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.puremvc.kotlin.demos.android.employeeadmin.controller.AppDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.data.AppDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

class RoleProxyTest: TestCase() {

    private lateinit var database: AppDatabase

    private lateinit var roleProxy: RoleProxy

    @Before
    override fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        roleProxy = RoleProxy(database.roleDAO(), handler)

        database.userDAO().insertAll(listOf(
            Department(1, "Accounting"),
            Department(2, "Sales"), Department(3, "Plant"),
            Department(4, "Shipping"), Department(5, "Quality Control")
        ))

        database.roleDAO().insertAll(listOf(Role(1, "Administrator"),
            Role(2, "Accounts Payable"), Role(3, "Accounts Receivable"),
            Role(4, "Employee Benefits"), Role(5, "General Ledger"),
            Role(6, "Payroll"), Role(7, "Inventory"),
            Role(8, "Production"), Role(9, "Quality Control"),
            Role(10, "Sales"), Role(11, "Orders"),
            Role(12, "Customers"), Role(13, "Shipping"),
            Role(14, "Returns")))

        database.userDAO().save(User(1, "lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", 3))
        database.userDAO().save(User(2, "cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", 4))
        database.userDAO().save(User(3, "mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", 5))

        database.roleDAO().insertUserRoles(listOf(
            UserRoleJoin(1, 4),
            UserRoleJoin(2, 3), UserRoleJoin(2, 5),
            UserRoleJoin(3, 8), UserRoleJoin(3, 10),
            UserRoleJoin(3, 13)
        ))
    }

    @Test
    fun testFindAll() {
        val roles = roleProxy.findAll()
        assertEquals(14, roles.size)
    }

    @Test
    fun testFindAllByUserId() {
        val roles = roleProxy.findByUserId(3)
        assertEquals(3, roles.size)
    }

    @Test
    fun testUpdateAndFindById() {
        roleProxy.updateByUserId(1, listOf(Role(1, "Administrator"), Role(2, "Accounts Payable")))
        val roles = roleProxy.findByUserId(1)
        assertEquals(2, roles.size)

        roleProxy.updateByUserId(1, listOf(Role(4, "Employee Benefits")))

        val roles2 = roleProxy.findByUserId(1)
        assertEquals(1, roles2.size)
    }

    @After
    override fun tearDown() {
        database.close()
        super.tearDown()
    }

}