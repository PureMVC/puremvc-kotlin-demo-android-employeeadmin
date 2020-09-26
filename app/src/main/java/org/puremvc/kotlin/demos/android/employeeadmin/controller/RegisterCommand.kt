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
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference

class RegisterCommand: SimpleCommand() {

    override fun execute(notification: INotification) {

        val fragment = notification.body as WeakReference<Fragment>

        if (fragment.get() is Fragment) {
            if (facade.hasMediator(EmployeeAdminMediator.NAME))
                facade.removeMediator(EmployeeAdminMediator.NAME)

            facade.registerMediator(EmployeeAdminMediator(fragment as WeakReference<Any?>))
        }

    }

}