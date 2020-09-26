//
//  UserVO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserVO(var id: Long, var username: String?, var first: String, var last: String, var email: String?, var password: String?, var department: Pair<Long, String>?): Parcelable {

    fun isValid(): Boolean? {
        return !(first == "" || last == "" || username == "" || password == "" || department?.first == null || department?.second == null)
    }

    override fun toString(): String {
        return "$last, $first"
    }

}
