//
//  RegisterCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import org.puremvc.kotlin.demos.android.employeeadmin.view.EmployeeAdminMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference

class RegisterCommand: SimpleCommand() {

    override fun execute(notification: INotification) {
        val name = EmployeeAdminMediator.NAME + "_" + notification.type
        if (facade.hasMediator(name)) facade.removeMediator(name)
        facade.registerMediator(EmployeeAdminMediator(name, notification.body as WeakReference<*>))
    }

}