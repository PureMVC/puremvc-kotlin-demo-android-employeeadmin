//
//  UserVO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum

@Parcelize
data class User(var username: String? = null, var first: String? = null, var last: String? = null, var email: String? = null, var password: String? = null, var department: DeptEnum? = DeptEnum.NONE_SELECTED): Parcelable {

    fun validate(confirm: String): String? {
        if (first == null || first == "" || last == null || last == "" ||
            username == null || username == "" || password == null || password == "" ||
            department == null || department?.ordinal == 0) {
            return "Invalid Form Data."
        }

        if (password != confirm) {
            return "Your password and confirmation password do not match."
        }
        return null
    }

    override fun toString(): String {
        return "$last, $first"
    }

}
