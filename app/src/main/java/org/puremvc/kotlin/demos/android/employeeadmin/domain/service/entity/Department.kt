//
//  Department.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Department(var id: Int, var name: String): Parcelable {

    fun validate(): Boolean {
        return id != 0
    }

}