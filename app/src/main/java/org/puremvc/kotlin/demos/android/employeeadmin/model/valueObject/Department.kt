//
//  Department.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.lang.Exception

@Parcelize
data class Department(var id: Long? = null, var name: String? = null): Parcelable {

    constructor(data: JSONObject) : this() {
        try { id = data.getLong("id") } catch (_: Exception) {}
        try { name = data.getString("name") } catch (_: Exception) {}
    }

    fun toJSONObject(): JSONObject {
        val data = JSONObject()
        data.put("id", id)
        data.put("name", name)
        return data
    }

    fun validate(): Boolean {
        return id != null && id != 0L
    }

}