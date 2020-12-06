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
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R

@Parcelize
data class User(var id: Long?, var username: String?, var first: String?, var last: String?, var email: String?, var password: String?, var department: Department?): Parcelable {

    fun validate(confirm: String): String? {
        if (first == "" || last == "" || username == "" || password == "" || confirm == "" || department == null || department?.id == 0L) {
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
