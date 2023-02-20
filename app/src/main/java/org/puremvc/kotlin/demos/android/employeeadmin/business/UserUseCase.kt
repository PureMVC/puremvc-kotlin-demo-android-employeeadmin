//
//  UserUseCase.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.business

class UserUseCase {

    operator fun invoke(body: Any?, type: String): Int {
        return 0
    }

//    override fun execute(notification: INotification) {
//        val name = EmployeeAdminMediator.NAME + "_" + notification.type
//        if (facade.hasMediator(name)) facade.removeMediator(name)
//        facade.registerMediator(EmployeeAdminMediator(name, notification.body as WeakReference<*>))
//    }

}
