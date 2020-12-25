//
//  User.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.database.Cursor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import java.lang.Exception

@Parcelize
data class User(var id: Long? = null, var username: String? = null, var first: String? = null, var last: String? = null, var email: String? = null, var password: String? = null, var department: Department? = null): Parcelable {

    constructor(cursor: Cursor) : this() {
        try { id = cursor.getLong(cursor.getColumnIndexOrThrow("id")) } catch (exception: Exception) {}
        try { username = cursor.getString(cursor.getColumnIndexOrThrow("username")) } catch (exception: Exception) {}
        try { first = cursor.getString(cursor.getColumnIndexOrThrow("first")) } catch (exception: Exception) {}
        try { last = cursor.getString(cursor.getColumnIndexOrThrow("last")) } catch (exception: Exception) {}
        try { email = cursor.getString(cursor.getColumnIndexOrThrow("email")) } catch (exception: Exception) {}
        try { password = cursor.getString(cursor.getColumnIndexOrThrow("password")) } catch (exception: Exception) {}
        try { department = Department(cursor.getLong(cursor.getColumnIndexOrThrow("department_id")), cursor.getString(cursor.getColumnIndexOrThrow("department_name"))) } catch (exception: Exception) {}
    }

    fun validate(confirm: String): String? {
        if (first == null || first == "" || last == null || last == "" ||
                username == null || username == "" || password == null || password == "" ||
                department == null || !department!!.validate()) {
            return Application.context?.resources?.getString(R.string.error_invalid_data)
        }

        if (password != confirm) {
            return Application.context?.resources?.getString(R.string.error_password)
        }
        return null
    }

    override fun toString(): String {
        return "$last, $first"
    }

}
