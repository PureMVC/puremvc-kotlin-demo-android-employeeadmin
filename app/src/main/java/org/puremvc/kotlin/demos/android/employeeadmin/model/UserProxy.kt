//
//  UserProxy.kt
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
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

    suspend fun findAll(): ArrayList<User>? = withContext(Dispatchers.IO) {
        var users: ArrayList<User>? = null
        connection.readableDatabase.query("user", arrayOf("id", "first", "last"), null, null, null, null, null).use { cursor ->
            if (cursor.count > 0) users = ArrayList()
            while (cursor.moveToNext()) {
                val user = User(cursor.getLong(cursor.getColumnIndexOrThrow("id")), null,
                    cursor.getString(cursor.getColumnIndexOrThrow("first")), cursor.getString(cursor.getColumnIndexOrThrow("last")),
                    null, null, null)
                users?.add(user)
            }
        }
        return@withContext users
    }

    suspend fun findById(id: Long): User? = withContext(Dispatchers.IO) {
        var user: User? = null
        connection.readableDatabase.rawQuery("SELECT user.*, department.name AS 'department_name' FROM user INNER JOIN department ON user.department_id = department.id WHERE user.id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToNext()) {
                user = User(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("first")), cursor.getString(cursor.getColumnIndexOrThrow("last")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")), cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    Department(cursor.getLong(cursor.getColumnIndexOrThrow("department_id")), cursor.getString(cursor.getColumnIndexOrThrow("department_name"))))
            }
        }
        return@withContext user
    }

    suspend fun save(user: User): Long = withContext(Dispatchers.IO) {
        val values = ContentValues()
        values.put("username", user.username)
        values.put("first", user.first)
        values.put("last", user.last)
        values.put("email", user.email)
        values.put("password", user.password)
        values.put("department_id", user.department?.id)
        return@withContext connection.writableDatabase.insertOrThrow("user", null, values)
    }

    suspend fun update(user: User): Int = withContext(Dispatchers.IO) {
        val values = ContentValues()
        values.put("first", user.first)
        values.put("last", user.last)
        values.put("email", user.email)
        values.put("password", user.password)
        values.put("department_id", user.department?.id)
        return@withContext connection.writableDatabase.update("user", values, "id = ?", arrayOf(user.id.toString()))
    }

    suspend fun deleteById(id: Long): Int = withContext(Dispatchers.IO) {
        return@withContext connection.writableDatabase.delete("user", "id = ?", arrayOf(id.toString()))
    }

    suspend fun findAllDepartments(): List<Department>? = withContext(Dispatchers.IO) {
        var departments: ArrayList<Department>? = null
        connection.readableDatabase.query("department", null, null, null, null, null, null).use { cursor ->
            if (cursor.count != 0) departments = ArrayList()
            while (cursor.moveToNext()) {
                departments?.add(Department(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("name"))))
            }
        }
        return@withContext departments
    }

}