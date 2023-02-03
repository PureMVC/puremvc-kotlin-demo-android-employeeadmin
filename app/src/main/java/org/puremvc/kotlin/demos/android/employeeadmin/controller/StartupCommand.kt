//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.model.*
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.UserDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.view.ApplicationMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference

@Database(entities = [User::class, Department::class, Role::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun roleDAO(): RoleDAO
}

class StartupCommand: SimpleCommand() {

    override fun execute(notification: INotification) {

        val application = notification.body as Application

        val database = Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "employeeadmin")
            // .createFromAsset("employeeadmin.db")
            .build()

        facade.registerProxy(UserProxy(database.userDAO()))
        facade.registerProxy(RoleProxy(database.roleDAO()))

        GlobalScope.launch {
            database.userDAO().findById(1)
        }

        facade.registerCommand(ApplicationFacade.REGISTER) { RegisterCommand() }
        facade.registerMediator(ApplicationMediator(WeakReference(application)))

    }

}