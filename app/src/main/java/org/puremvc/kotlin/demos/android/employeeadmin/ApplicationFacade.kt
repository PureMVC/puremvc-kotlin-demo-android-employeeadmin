//
//  ApplicationFacade.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin

import androidx.fragment.app.Fragment
import org.puremvc.kotlin.demos.android.employeeadmin.controller.RegisterCommand
import org.puremvc.kotlin.demos.android.employeeadmin.controller.StartupCommand
import org.puremvc.kotlin.multicore.patterns.facade.Facade
import java.lang.ref.WeakReference

class ApplicationFacade(key: String) : Facade(key) {

    companion object {

        const val KEY: String = "EmployeeAdmin"

        const val STARTUP: String = "startup"

        const val REGISTER: String = "register"

        fun getInstance(key: String): ApplicationFacade {
            return Facade.getInstance(key) { k -> ApplicationFacade(k) } as ApplicationFacade
        }
    }

    override fun initializeController() {
        super.initializeController()
        registerCommand(STARTUP) { StartupCommand() }
        registerCommand(REGISTER) { RegisterCommand() }
    }

    fun registerView(fragment: WeakReference<Fragment>) {
        sendNotification(REGISTER, fragment)
    }

    fun startup(application: Application) {
        sendNotification(STARTUP, application)
    }

}