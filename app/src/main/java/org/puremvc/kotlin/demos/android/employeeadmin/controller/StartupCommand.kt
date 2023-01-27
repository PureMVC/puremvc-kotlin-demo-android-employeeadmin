//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.view.ApplicationMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class StartupCommand: SimpleCommand() {

    override fun execute(notification: INotification) {
        val application = notification.body as Application

        val factory: (URL) -> HttpURLConnection  = { url ->
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 2500
            connection.connectTimeout = 2500
            connection
        }

        facade.registerProxy(UserProxy(factory))
        facade.registerProxy(RoleProxy(factory))

        facade.registerCommand(ApplicationFacade.REGISTER) { RegisterCommand() }
        facade.registerMediator(ApplicationMediator(WeakReference(application)))
    }

}