//
//  RoleVO.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum

@Parcelize
data class Role(val username: String? = null, var roles: ArrayList<RoleEnum>? = null): Parcelable