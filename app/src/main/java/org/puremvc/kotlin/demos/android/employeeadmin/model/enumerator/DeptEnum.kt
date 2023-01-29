//
//  DeptEnum.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DeptEnum: Parcelable {

    NONE_SELECTED { override fun toString() = "--None Selected--" },
    ACCT { override fun toString() = "Accounting" },
    SALES { override fun toString() = "Sales" },
    PLANT { override fun toString() = "Plant" },
    SHIPPING { override fun toString() = "Shipping" },
    QC { override fun toString() = "Quality Control" };

}