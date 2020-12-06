//
//  Application.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class Application: Application() {

    private val facade by lazy { ApplicationFacade.getInstance("EmployeeAdmin") as ApplicationFacade }

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        facade.startup(this)
    }

    fun register(fragment: WeakReference<Fragment>) {
        facade.register(fragment)
    }

}