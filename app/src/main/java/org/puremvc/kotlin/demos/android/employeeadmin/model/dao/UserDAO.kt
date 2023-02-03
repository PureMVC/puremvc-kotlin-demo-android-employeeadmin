package org.puremvc.kotlin.demos.android.employeeadmin.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserDepartment

@Dao
interface UserDAO {
//    @Query("SELECT id, first, last from user")
//    fun findAll(): List<User>
//
//    @Query("SELECT id, first, last from user")
//    fun findAll2(): LiveData<User>

//    @Query("SELECT user.*, department.name AS 'department_name' FROM user " +
//            "INNER JOIN department ON user.department_id = department.id " +
//            "WHERE user.id = :id")
    @Query("SELECT * FROM user WHERE id = :id")
    fun findById(id: Long): UserDepartment

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun save(users: User): Long
//
//    @Update
//    fun update(user: User): Int
//
//    @Query("DELETE FROM user WHERE id = :id")
//    fun deleteById(id: Long): Int
//
//    @Query("SELECT id, name FROM department")
//    fun findAllDepartments(): List<Department>
}