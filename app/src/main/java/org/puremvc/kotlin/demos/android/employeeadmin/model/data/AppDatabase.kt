package org.puremvc.kotlin.demos.android.employeeadmin.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserRoleJoin

@Database(entities = [User::class, Department::class, Role::class, UserRoleJoin::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun roleDAO(): RoleDAO
}