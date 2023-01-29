//
//  RoleEnum.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RoleEnum: Parcelable {

    ADMIN { override fun toString() = "Administrator" },
    ACCT_PAY { override fun toString() = "Accounts Payable" },
    ACCT_RCV { override fun toString() = "Accounts Receivable" },
    EMP_BENEFITS { override fun toString() = "Employee Benefits" },
    GEN_LEDGER { override fun toString() = "General Ledger" },
    PAYROLL { override fun toString() = "Payroll" },
    INVENTORY { override fun toString() = "Inventory" },
    PRODUCTION { override fun toString() = "Production" },
    QUALITY_CTL { override fun toString() = "Quality Control" },
    SALES { override fun toString() = "Sales" },
    ORDERS { override fun toString() = "Orders" },
    CUSTOMERS { override fun toString() = "Customers" },
    SHIPPING { override fun toString() = "Shipping" },
    RETURNS { override fun toString() = "Returns" };

}