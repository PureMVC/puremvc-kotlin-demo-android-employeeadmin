//
//  RegisterCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import androidx.appcompat.app.AppCompatActivity
import org.puremvc.kotlin.demos.android.employeeadmin.view.UserListMediator
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserListActivity
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand

class RegisterCommand: SimpleCommand() {

    override fun execute(notification: INotification) {
        val activity = notification.body as AppCompatActivity

        if (activity is UserListActivity) {
            facade.registerMediator(UserListMediator(activity))
        }
    }

}