package org.puremvc.kotlin.demos.android.employeeadmin.model.dao

import androidx.room.Dao
import androidx.room.Query
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role

@Dao
interface RoleDAO {
    @Query("SELECT id, name FROM role")
    fun findAll(): List<Role>

    @Query("SELECT id, name FROM role WHERE id = :id") // INNER JOIN user_role ON role.id = user_role.role_id
    fun findByUserId(id: Long): List<Role>

//    @Query("DELETE FROM user_role WHERE user_id = :id")
//    fun deleteById(id: Long)

//    @Insert
//    fun insert(userId: Long, roleId: Long)
//
//    @Transaction
//    fun updateByUserId(id: Long, role: Role) {
//        deleteById(id)
//        insert(id, role.id)
//    }
}