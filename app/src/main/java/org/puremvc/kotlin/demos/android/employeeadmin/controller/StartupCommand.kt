//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import android.util.Log
import android.widget.Toast
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.connections.SQLite
import org.puremvc.kotlin.demos.android.employeeadmin.view.ApplicationMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.Exception
import java.lang.ref.WeakReference

class StartupCommand: SimpleCommand() {

    override fun execute(notification: INotification) {

        val application = notification.body as Application

        val connection = SQLite(application.applicationContext, "employeeadmin.db", null, 1)

        try {
            connection.readableDatabase.use {
                facade.run {
                    registerProxy(UserProxy(connection))
                    registerProxy(RoleProxy(connection))

                    registerCommand(ApplicationFacade.REGISTER) { RegisterCommand() }
                    registerMediator(ApplicationMediator(WeakReference(application)))
                }
            }
        } catch (exception: Exception) {
            Log.d("StartupCommand", "execute: ${exception.localizedMessage}")
            Toast.makeText(application.applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

}