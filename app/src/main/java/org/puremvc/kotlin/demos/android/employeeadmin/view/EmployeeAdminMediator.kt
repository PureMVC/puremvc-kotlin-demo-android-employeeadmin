//
//  EmployeeAdminMediator.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.puremvc.kotlin.demos.android.employeeadmin.model.RoleProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.UserProxy
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.*
import org.puremvc.kotlin.multicore.patterns.mediator.Mediator
import java.lang.ref.WeakReference

class EmployeeAdminMediator(name: String, override var viewComponent: WeakReference<*>?): Mediator(name, viewComponent), IUserList, IUserForm, IUserRole {

    companion object {
        const val NAME: String = "EmployeeAdminMediator"
    }

    private var userProxy: UserProxy? = null
    private var roleProxy: RoleProxy? = null

    override fun onRegister() {
        userProxy = facade.retrieveProxy(UserProxy.NAME) as? UserProxy
        roleProxy = facade.retrieveProxy(RoleProxy.NAME) as? RoleProxy

        when (val view = viewComponent?.get()) {
            is UserList -> view.setDelegate(this)
            is UserForm -> view.setDelegate(this)
            is UserRole -> view.setDelegate(this)
        }
    }

    override suspend fun findAll(): List<User>? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.findAll()
    }

    override suspend fun deleteById(id: Int): Int? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.deleteById(id)
    }

    override suspend fun findById(id: Int): User? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.findById(id)
    }

    override suspend fun save(user: User, roles: List<Role>?): User? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.save(user)?.also { user ->
            roles?.let { roleProxy?.insertUserRoles(user.id, it) }
        }
    }

    override suspend fun update(user: User, roles: List<Role>?): User? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.update(user)?.also {
            roles?.let { roleProxy?.updateByUserId(user.id, it) }
        }
    }

    override suspend fun findAllDepartments(): List<Department>? = withContext(Dispatchers.IO) {
        return@withContext userProxy?.findAllDepartments()
    }

    override suspend fun findAllRoles() = withContext(Dispatchers.IO) {
        return@withContext roleProxy?.findAll()
    }

    override suspend fun findRolesById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext roleProxy?.findByUserId(id)
    }

    override fun remove() {
        facade.removeMediator(name)
    }

}