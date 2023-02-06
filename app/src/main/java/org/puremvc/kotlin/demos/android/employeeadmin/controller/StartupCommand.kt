//
//  StartupCommand.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.controller

import androidx.appcompat.app.AlertDialog
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.RoleDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.dao.UserDAO
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.*
import org.puremvc.kotlin.demos.android.employeeadmin.view.ApplicationMediator
import org.puremvc.kotlin.multicore.interfaces.INotification
import org.puremvc.kotlin.multicore.patterns.command.SimpleCommand
import java.lang.ref.WeakReference

@Database(entities = [User::class, Department::class, Role::class, UserRoleJoin::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun roleDAO(): RoleDAO
}

class StartupCommand: SimpleCommand() {

    override fun execute(notification: INotification) {

        val application = notification.body as Application

        var initialized = true
        val database = Room.
            databaseBuilder(application.applicationContext, AppDatabase::class.java, "employeeadmin.sqlite")
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initialized = false
                }
            })
            .build()

        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler{ _, e, ->
            AlertDialog.Builder(application).setTitle(e.javaClass.simpleName)
                .setMessage(e.cause?.message).setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.okay, null).create().show()
        }).launch {
            database.runInTransaction {} // force database creation
        }.invokeOnCompletion {
            if (!initialized) {
                database.userDAO().insertAll(listOf(Department(1, "Accounting"),
                    Department(2, "Sales"), Department(3, "Plant"),
                    Department(4, "Shipping"), Department(5, "Quality Control")))

                database.roleDAO().insertAll(listOf(Role(1, "Administrator"),
                    Role(2, "Accounts Payable"), Role(3, "Accounts Receivable"),
                    Role(4, "Employee Benefits"), Role(5, "General Ledger"),
                    Role(6, "Payroll"), Role(7, "Inventory"),
                    Role(8, "Production"), Role(9, "Quality Control"),
                    Role(10, "Sales"), Role(11, "Orders"),
                    Role(12, "Customers"), Role(13, "Shipping"),
                    Role(14, "Returns")))

                database.userDAO().save(User(1, "lstooge", "Larry", "Stooge", "larry@stooges.com", "ijk456", 3))
                database.userDAO().save(User(2, "cstooge", "Curly", "Stooge", "curly@stooges.com", "xyz987", 4))
                database.userDAO().save(User(3, "mstooge", "Moe", "Stooge", "moe@stooges.com", "abc123", 5))

                database.roleDAO().insertUserRoles(listOf(UserRoleJoin(1, 4),
                    UserRoleJoin(2, 3), UserRoleJoin(2, 5),
                    UserRoleJoin(3, 8), UserRoleJoin(3, 10),
                    UserRoleJoin(3, 13)))
            }
        }

        facade.registerProxy(UserProxy(database.userDAO()))
        facade.registerProxy(RoleProxy(database.roleDAO()))

        facade.registerCommand(ApplicationFacade.REGISTER) { RegisterCommand() }
        facade.registerMediator(ApplicationMediator(WeakReference(application)))
    }

}