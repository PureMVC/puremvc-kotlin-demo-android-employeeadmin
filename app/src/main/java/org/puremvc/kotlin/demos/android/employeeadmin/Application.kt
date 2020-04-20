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
import java.lang.ref.WeakReference

class Application: Application() {

    private val facade by lazy { ApplicationFacade.getInstance("EmployeeAdmin") as ApplicationFacade }

    override fun onCreate() {
        super.onCreate()
        facade.startup(this)
    }

    fun registerActivity(activity: WeakReference<AppCompatActivity>) {
        facade.registerActivity(activity)
    }

}