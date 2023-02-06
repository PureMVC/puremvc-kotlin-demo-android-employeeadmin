//
//  RoleDAO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.dao

import androidx.room.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

@Dao
interface RoleDAO {

    @Query("SELECT id, name FROM role")
    fun findAll(): List<Role>

    @Transaction
    @Query("SELECT id, name FROM role " +
            "INNER JOIN user_role ON role.id = user_role.role_id " +
            "WHERE user_id = :id")
    fun findByUserId(id: Long): List<Role>

    @Insert
    fun insertAll(roles: List<Role>)

    @Insert
    fun insertUserRoles(roles: List<UserRoleJoin>)

    @Transaction
    fun updateByUserId(id: Long, roles: List<UserRoleJoin>) {
        deleteById(id)
        insertUserRoles(roles)
    }

    @Query("DELETE FROM user_role WHERE user_id = :id")
    fun deleteById(id: Long)

}