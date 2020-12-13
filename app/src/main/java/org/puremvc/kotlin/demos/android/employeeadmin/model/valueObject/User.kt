//
//  User.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import java.lang.Exception

@Parcelize
data class User(var id: Long? = null, var username: String? = null, var first: String? = null, var last: String? = null, var email: String? = null, var password: String? = null, var department: Department? = null): Parcelable {

    constructor(data: JSONObject) : this() {
        try { id = data.getLong("id") } catch (exception: Exception) {}
        try { username = data.getString("username") } catch (exception: Exception) {}
        try { first = data.getString("first") } catch (exception: Exception) {}
        try { last = data.getString("last") } catch (exception: Exception) {}
        try { email = data.getString("email") } catch (exception: Exception) {}
        try { password = data.getString("password") } catch (exception: Exception) {}
        try { department = Department(data.getJSONObject("department")) } catch (exception: Exception) {}
    }

    fun validate(confirm: String): String? {
        if (first == "" || last == "" || username == "" || password == "" || confirm == "" || department == null || department?.id == 0L) {
            return Application.context?.resources?.getString(R.string.error_invalid_data)
        }
        if (password != confirm) {
            return Application.context?.resources?.getString(R.string.error_password)
        }
        return null
    }

    fun toJSONObject(): JSONObject {
        val data = JSONObject()
        data.put("id", id)
        data.put("username", username)
        data.put("first", first)
        data.put("last", last)
        data.put("email", email)
        data.put("password", password)
        data.put("department", department?.toJSONObject())
        return data
    }

    override fun toString(): String {
        return "$last, $first"
    }

}
