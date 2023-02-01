//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.flow.Flow
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy

class UserProxy(private val connection: SQLiteOpenHelper): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

    fun findAll(): List<User> {
        val sql = "SELECT id, first, last FROM user"
        var users: ArrayList<User> = arrayListOf()
        connection.readableDatabase.rawQuery(sql, null).use { cursor ->
            if (cursor.count > 0) users = ArrayList()
            while (cursor.moveToNext()) {
                users.add(User(cursor))
            }
        }
        return users
    }

    fun findById(id: Long): User? {
        var user: User? = null
        connection.readableDatabase.rawQuery("SELECT user.*, department.name AS 'department_name' FROM user INNER JOIN department ON user.department_id = department.id WHERE user.id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToNext()) {
                user = User(cursor)
            }
        }
        return user
    }

    fun save(user: User): Long {
        val sql = "INSERT INTO user(username, first, last, email, password, department_id) VALUES(?, ?, ?, ?, ?, ?)"
        connection.writableDatabase.execSQL(sql, arrayOf(user.username, user.first, user.last, user.email, user.password, user.department?.id.toString()))

        connection.readableDatabase.rawQuery("SELECT last_insert_rowid()", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getLong(0)
        }
    }

    fun update(user: User): Int {
        val sql = "UPDATE user SET first = ?, last = ?, email = ?, password = ?, department_id = ? WHERE id = ?"
        connection.writableDatabase.execSQL(sql, arrayOf(user.first, user.last, user.email, user.password, user.department?.id.toString(), user.id.toString()))

        connection.readableDatabase.rawQuery("SELECT changes()", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    fun deleteById(id: Long): Int {
        val sql = "DELETE FROM user WHERE id = ?"
        connection.writableDatabase.execSQL(sql, arrayOf(id.toString()))

        connection.readableDatabase.rawQuery("SELECT changes()", null).use { cursor ->
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
    }

    fun findAllDepartments(): List<Department> {
        val sql = "SELECT id, name FROM department"
        val departments: ArrayList<Department> = arrayListOf()
        connection.readableDatabase.rawQuery(sql, null).use { cursor ->
            while (cursor.moveToNext()) {
                departments.add(Department(cursor))
            }
        }
        return departments
    }

}