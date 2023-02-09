//
//  UserDAO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun findAll(): LiveData<List<User>>

    @Query("SELECT * FROM user " +
            "INNER JOIN department ON user.department_id = department.id " +
            "WHERE user.id = :id")
    suspend fun findById(id: Long): Map<User, Department>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(users: User): Long

    @Update
    suspend fun update(user: User): Int

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("SELECT * FROM department")
    suspend fun findAllDepartments(): List<Department>

}