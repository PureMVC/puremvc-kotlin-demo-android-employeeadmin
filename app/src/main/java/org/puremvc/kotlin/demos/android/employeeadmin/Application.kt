//
//  Application.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin

import android.app.Application
import android.content.res.Configuration

interface IApplication {
    fun onConfigurationChanged(config: Configuration)
    fun onCreate()
    fun onTerminate()
    fun onTrimMemory(level: Int)
    fun onLowMemory()
}

class Application: Application() {

    private var delegate: IApplication? = null

    init {
        ApplicationFacade.getInstance(ApplicationFacade.KEY).startup(this)
    }

    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)
        delegate?.onConfigurationChanged(config)
    }

    override fun onCreate() {
        super.onCreate()
        delegate?.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        delegate?.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        delegate?.onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        delegate?.onLowMemory()
    }

    fun setDelegate(delegate: IApplication) {
        this.delegate = delegate
    }

}