//
//  EmployeeAdmin.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.presentation.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.test.espresso.idling.CountingIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import org.puremvc.kotlin.demos.android.employeeadmin.R

@AndroidEntryPoint
class EmployeeAdmin: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_admin)
        setupActionBarWithNavController(findNavController(R.id.nav_graph))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_graph).navigateUp() || super.onSupportNavigateUp()
    }

}

object IdlingResource {

    val counter = CountingIdlingResource("Espresso")

    fun increment() {
        counter.increment()
    }

    fun decrement() {
        if (!counter.isIdleNow) {
            counter.decrement()
        }
    }
}
