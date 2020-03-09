//
//  DeleteUserCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand

class DeleteUserCommand: SimpleCommand() {

    override fun execute(notification: INotification) {
        val userVO = notification.body as UserVO
        val userProxy = facade.retrieveProxy(UserProxy.NAME) as UserProxy
        val roleProxy = facade.retrieveProxy(RoleProxy.NAME) as RoleProxy
        userProxy.deleteItem(userVO)
        roleProxy.deleteItem(userVO.username)
    }

}