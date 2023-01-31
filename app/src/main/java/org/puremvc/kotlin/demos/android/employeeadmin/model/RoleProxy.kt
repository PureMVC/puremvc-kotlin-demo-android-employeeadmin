//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.database.sqlite.SQLiteOpenHelper
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import java.lang.Exception

class RoleProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    fun findAll(): List<Role> {
        val sql = "SELECT id, name FROM role"
        val roles: ArrayList<Role> = arrayListOf()
        connection.readableDatabase.rawQuery(sql, null).use { cursor ->
            while (cursor.moveToNext()) {
                roles.add(Role(cursor))
            }
        }
        return roles
    }

    fun findByUserId(id: Long): ArrayList<Role> {
        val roles: ArrayList<Role> = arrayListOf()
        connection.readableDatabase.rawQuery("SELECT id, name FROM role INNER JOIN user_role ON role.id = user_role.role_id WHERE user_id = ?", arrayOf(id.toString())).use { cursor ->
            while (cursor.moveToNext()) {
                roles.add(Role(cursor))
            }
        }
        return roles
    }

    fun updateByUserId(id: Long, roles: List<Role>?): Int {
        try {
            connection.writableDatabase.execSQL("BEGIN TRANSACTION;")
            connection.writableDatabase.execSQL("DELETE FROM user_role WHERE user_id = $id;")

            val values = (roles?.map { role -> "($id, ${role.id})" })?.joinToString(",") ?: ""

            if (values.isNotEmpty()) connection.writableDatabase.execSQL("INSERT INTO user_role(user_id, role_id) VALUES$values")
            connection.writableDatabase.execSQL("COMMIT")
        } catch(exception: Exception) {
            connection.writableDatabase.execSQL("ROLLBACK")
            throw exception
        }

        return connection.readableDatabase.rawQuery("SELECT changes()", null).use { cursor ->
            return@use cursor.count
        }
    }

}
