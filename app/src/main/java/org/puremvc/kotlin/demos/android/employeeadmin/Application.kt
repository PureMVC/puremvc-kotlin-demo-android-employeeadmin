//
//  Application.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin

import android.app.Application

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationFacade.getInstance(ApplicationFacade.KEY).startup(this)
    }

}