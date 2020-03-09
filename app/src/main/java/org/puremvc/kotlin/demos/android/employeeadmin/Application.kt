//
//  Application.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class Application: Application() {

    companion object {
        const val ACTIVITY_USER_FORM = 1

        const val ACTIVITY_USER_ROLE = 2

        const val BUNDLE_USER = "BUNDLE_USER"

        const val BUNDLE_USER_ROLE = "BUNDLE_USER_ROLE"
    }

    private val facade = ApplicationFacade.getInstance("EmployeeAdmin") as ApplicationFacade

    override fun onCreate() {
        super.onCreate()
        facade.startup(this)
    }

    fun registerActivity(activity: AppCompatActivity) {
        facade.registerActivity(activity)
    }

}