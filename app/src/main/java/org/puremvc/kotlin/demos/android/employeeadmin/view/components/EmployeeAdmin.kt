//
//  EmployeeAdmin.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.puremvc.kotlin.demos.android.employeeadmin.R

class EmployeeAdmin: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_admin)
        supportActionBar?.title = "Employee Admin"
    }

    fun fault(exception: Throwable) {
        AlertDialog.Builder(this)
            .setTitle(exception.javaClass.simpleName)
            .setMessage(exception.localizedMessage)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

}
