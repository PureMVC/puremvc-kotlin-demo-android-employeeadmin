//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import java.lang.Exception

class RoleProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    suspend fun findAll(): List<Role>? = withContext(Dispatchers.IO) {
        var roles: ArrayList<Role>? = null
        connection.readableDatabase.query("role", null, null, null, null, null, null).use { cursor ->
            if (cursor.count > 0) roles = ArrayList()
            while (cursor.moveToNext()) {
                roles?.add(Role(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("name"))))
            }
        }
        return@withContext roles
    }

    suspend fun findAllByUserId(id: Long): ArrayList<Role>? = withContext(Dispatchers.IO) {
        var roles: ArrayList<Role>? = null
        connection.readableDatabase.rawQuery("SELECT id, name FROM role INNER JOIN user_role ON role.id = user_role.role_id WHERE user_id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.count > 0) roles = ArrayList()
            while (cursor.moveToNext()) {
                roles?.add(Role(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("name"))))
            }
        }
        return@withContext roles
    }

    suspend fun updateRolesByUserId(id: Long, roles: List<Role>): ArrayList<Long>? = withContext(Dispatchers.IO) {
        var ids: ArrayList<Long>? = null
        val database = connection.writableDatabase
        try {
            database.beginTransaction()
            database.delete("user_role", "user_id = ?", arrayOf(id.toString()))
            if (roles.count() > 0) ids = ArrayList()
            roles.forEach { role ->
                val values = ContentValues()
                values.put("user_id", id)
                values.put("role_id", role.id)
                ids?.add(database.insertOrThrow("user_role", null, values))
            }
            database.setTransactionSuccessful()
        } catch (exception: Exception) {
            throw exception
        } finally {
            database.endTransaction()
        }
        return@withContext ids
    }

}