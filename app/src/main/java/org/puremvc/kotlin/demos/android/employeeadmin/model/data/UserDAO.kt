//
//  UserDAO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.data

import androidx.room.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRole

@Dao
interface UserDAO {

    @Query("SELECT * from user")
    fun findAll(): List<User>

    @Query("SELECT * FROM user " +
            "INNER JOIN department ON user.department_id = department.id " +
            "WHERE user.id = :id")
    fun findById(id: Long): Map<User, Department>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(users: User): Long

    @Update
    fun update(user: User): Int

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("SELECT * FROM department")
    fun findAllDepartments(): List<Department>

    @Insert
    fun insertAll(departments: List<Department>)

    @Transaction
    @Query("SELECT * FROM user")
    fun getUserRoles(): List<UserRole>

}