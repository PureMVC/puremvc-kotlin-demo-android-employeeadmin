//
//  Role.kt
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
data class Role(var id: Long? = null, var name: String? = null): Parcelable {

    constructor(cursor: Cursor) : this() {
        try { id = cursor.getLong(cursor.getColumnIndexOrThrow("id")) } catch (_: Exception) {}
        try { name = cursor.getString(cursor.getColumnIndexOrThrow("name")) } catch (_: Exception) {}
    }

}