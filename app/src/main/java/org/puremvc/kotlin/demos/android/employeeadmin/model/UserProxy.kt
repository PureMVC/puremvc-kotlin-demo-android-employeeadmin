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
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

    suspend fun findAll(): ArrayList<UserVO>? = withContext(Dispatchers.IO) {
        var users: ArrayList<UserVO>? = null
        connection.readableDatabase.query("employee", arrayOf("id", "first", "last"), null, null, null, null, null).use { cursor ->
            if (cursor.count > 0) users = ArrayList()
            while (cursor.moveToNext()) {
                val user = UserVO(cursor.getLong(cursor.getColumnIndex("id")), null,
                    cursor.getString(cursor.getColumnIndex("first")), cursor.getString(cursor.getColumnIndex("last")),
                    null, null, null)
                users?.add(user)
            }
        }
        return@withContext users
    }

    suspend fun findById(id: Long): UserVO? = withContext(Dispatchers.IO) {
        var user: UserVO? = null
        connection.readableDatabase.rawQuery("SELECT employee.*, department.name AS 'department_name' FROM employee INNER JOIN department ON employee.departmentId = department.id WHERE employee.id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToNext()) {
                user = UserVO(cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("first")), cursor.getString(cursor.getColumnIndex("last")),
                    cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getLong(cursor.getColumnIndex("departmentId")) to cursor.getString(cursor.getColumnIndex("department_name")))
            }
        }
        return@withContext user
    }

    suspend fun save(user: UserVO): Long = withContext(Dispatchers.IO) {
        val values = ContentValues()
        values.put("username", user.username)
        values.put("first", user.first)
        values.put("last", user.last)
        values.put("email", user.email)
        values.put("password", user.password)
        values.put("departmentId", user.department?.first)
        return@withContext connection.writableDatabase.insertOrThrow("employee", null, values)
    }

    suspend fun update(user: UserVO): Int = withContext(Dispatchers.IO) {
        val values = ContentValues()
        values.put("first", user.first)
        values.put("last", user.last)
        values.put("email", user.email)
        values.put("password", user.password)
        values.put("departmentId", user.department?.first)
        return@withContext connection.writableDatabase.update("employee", values, "id = ?", arrayOf(user.id.toString()))
    }

    suspend fun deleteById(id: Long): Int = withContext(Dispatchers.IO) {
        return@withContext connection.writableDatabase.delete("employee", "id = ?", arrayOf(id.toString()))
    }

    suspend fun findAllDepartments(): HashMap<Long, String>? = withContext(Dispatchers.IO) {
        var departments: HashMap<Long, String>? = null
        connection.readableDatabase.query("department", null, null, null, null, null, null).use { cursor ->
            if (cursor.count != 0) departments = HashMap()
            while (cursor.moveToNext()) {
                departments?.put(cursor.getLong(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")))
            }
        }
        return@withContext departments
    }

}