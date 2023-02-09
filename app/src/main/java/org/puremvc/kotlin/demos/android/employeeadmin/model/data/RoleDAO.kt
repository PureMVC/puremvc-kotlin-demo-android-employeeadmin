//
//  RoleDAO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.data

import androidx.room.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

@Dao
interface RoleDAO {

    @Query("SELECT id, name FROM role")
    suspend fun findAll(): List<Role>

    @Transaction
    @Query("SELECT id, name FROM role " +
            "INNER JOIN user_role ON role.id = user_role.role_id " +
            "WHERE user_id = :id")
    suspend fun findByUserId(id: Long): List<Role>

    @Insert
    suspend fun insertUserRoles(roles: List<UserRoleJoin>)

    @Transaction
    suspend fun updateByUserId(id: Long, roles: List<UserRoleJoin>) {
        deleteById(id)
        insertUserRoles(roles)
    }

    @Query("DELETE FROM user_role WHERE user_id = :id")
    suspend fun deleteById(id: Long)

}