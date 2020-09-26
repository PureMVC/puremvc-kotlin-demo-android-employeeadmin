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
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import java.lang.Exception

class RoleProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    suspend fun findAll(): HashMap<Long, String>? = withContext(Dispatchers.IO) {
        var roles: HashMap<Long, String>? = null
        connection.readableDatabase.query("role", null, null, null, null, null, null).use { cursor ->
            if (cursor.count > 0) roles = HashMap()
            while (cursor.moveToNext()) {
                roles?.put(cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")))
            }
        }
        return@withContext roles
    }

    suspend fun findAllByUserId(id: Long): HashMap<Long, String>? = withContext(Dispatchers.IO) {
        var roles: HashMap<Long, String>? = null
        connection.readableDatabase.rawQuery("SELECT id, name FROM role INNER JOIN employee_role ON role.id = employee_role.role_id WHERE employee_id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.count > 0) roles = HashMap()
            while (cursor.moveToNext()) {
                roles?.put(cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")))
            }
        }
        return@withContext roles
    }

    suspend fun updateRolesByUserId(id: Long, roles: HashMap<Long, String>): ArrayList<Long>? = withContext(Dispatchers.IO) {
        var ids: ArrayList<Long>? = null
        val database = connection.writableDatabase
        try {
            database.beginTransaction()
            database.delete("employee_role", "employee_id = ?", arrayOf(id.toString()))
            if (roles.count() > 0) ids = ArrayList()
            roles.forEach { role ->
                val values = ContentValues()
                values.put("employee_id", id)
                values.put("role_id", role.key)
                ids?.add(database.insertOrThrow("employee_role", null, values))
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