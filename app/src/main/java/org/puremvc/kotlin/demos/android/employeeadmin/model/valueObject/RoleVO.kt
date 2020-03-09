//
//  RoleVO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum

@Parcelize
data class RoleVO(val username: String, var roles: ArrayList<RoleEnum>): Parcelable