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
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum

@Parcelize
data class UserVO(var username: String, var first: String, var last: String, var email: String, var password: String, var department: DeptEnum = DeptEnum.NONE_SELECTED): Parcelable {

    fun isValid(): Boolean? {
        return !(first == "" || last == "" || username == "" || password == "" || department.ordinal == DeptEnum.NONE_SELECTED.ordinal)
    }

    override fun toString(): String {
        return "$last, $first"
    }

}
