//
//  PrepModelCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.DeptEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.enumerator.RoleEnum
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.RoleVO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand

class PrepModelCommand: SimpleCommand() {

    /**
     * Prepare the Model.
     */
    override fun execute(notification: INotification) {
        // Create User Proxy,
        val userProxy = UserProxy()

        //Populate it with dummy data
        userProxy.addItem(UserVO("lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", DeptEnum.ACCT))
        userProxy.addItem(UserVO("cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", DeptEnum.SALES))
        userProxy.addItem(UserVO("mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", DeptEnum.PLANT))

        // register it
        facade.registerProxy(userProxy)

        // Create Role Proxy
        val roleProxy = RoleProxy()

        //Populate it with dummy data
        roleProxy.addItem(RoleVO("lstooge", arrayListOf(RoleEnum.EMP_BENEFITS)))
        roleProxy.addItem(RoleVO("cstooge", arrayListOf(RoleEnum.ACCT_RCV, RoleEnum.GEN_LEDGER)))
        roleProxy.addItem(RoleVO("mstooge", arrayListOf(RoleEnum.PRODUCTION, RoleEnum.SALES, RoleEnum.SHIPPING)))

        // register it
        facade.registerProxy(roleProxy)
    }

}