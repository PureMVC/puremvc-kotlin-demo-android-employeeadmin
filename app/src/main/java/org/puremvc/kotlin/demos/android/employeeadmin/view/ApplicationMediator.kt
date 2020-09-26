//
//  ApplicationMediator.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class ApplicationMediator(override var viewComponent: WeakReference<Any?>?): Mediator(NAME, viewComponent) {

    companion object {
        const val NAME: String = "ApplicationMediator"
    }

}