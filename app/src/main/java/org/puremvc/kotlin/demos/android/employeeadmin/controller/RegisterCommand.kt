//
//  RegisterCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import androidx.fragment.app.Fragment
import org.puremvc.kotlin.demos.android.employeeadmin.view.EmployeeAdminMediator
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserForm
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserList
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.UserRole
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference

class RegisterCommand: SimpleCommand() {

    override fun execute(notification: INotification) {

        when((notification.body as WeakReference<*>).get()) {
            is UserList -> {
                val name = EmployeeAdminMediator.NAME + "_UserList"
                if (facade.hasMediator(name)) facade.removeMediator(name)
                facade.registerMediator(EmployeeAdminMediator(name, notification.body as WeakReference<*>))
            }
            is UserForm -> {
                val name = EmployeeAdminMediator.NAME + "_UserForm"
                if (facade.hasMediator(name)) facade.removeMediator(name)
                facade.registerMediator(EmployeeAdminMediator(name, notification.body as WeakReference<*>))
            }
            is UserRole -> {
                val name = EmployeeAdminMediator.NAME + "_UserRole"
                if (facade.hasMediator(name)) facade.removeMediator(name)
                facade.registerMediator(EmployeeAdminMediator(name, notification.body as WeakReference<*>))
            }
        }

    }

}