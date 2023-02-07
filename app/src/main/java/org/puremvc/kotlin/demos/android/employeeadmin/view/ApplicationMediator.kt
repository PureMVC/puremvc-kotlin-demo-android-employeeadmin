//
//  ApplicationMediator.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

import android.content.res.Configuration
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.IApplication
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class ApplicationMediator(override var viewComponent: WeakReference<*>?): Mediator(NAME, viewComponent), IApplication {

    companion object {
        const val NAME: String = "ApplicationMediator"
    }

    override fun onConfigurationChanged(config: Configuration) {}

    override fun onCreate() {}

    override fun onTerminate() {}

    override fun onTrimMemory(level: Int) {}

    override fun onLowMemory() {}

    override fun onRegister() {
        (this.viewComponent?.get() as Application).setDelegate(this)
    }

}