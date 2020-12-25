//
//  Department.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.database.Cursor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.lang.Exception

@Parcelize
data class Department(var id: Long? = null, var name: String? = null): Parcelable {

    constructor(cursor: Cursor) : this() {
        try { id = cursor.getLong(cursor.getColumnIndexOrThrow("id")) } catch (exception: Exception) {}
        try { name = cursor.getString(cursor.getColumnIndexOrThrow("name")) } catch (exception: Exception) {}
    }

    fun validate(): Boolean {
        return id != null && id != 0L
    }

}