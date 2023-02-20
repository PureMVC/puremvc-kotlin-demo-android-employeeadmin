//
//  EmployeeAdminViewModel.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.puremvc.kotlin.demos.android.employeeadmin.domain.RoleRepository
import org.puremvc.kotlin.demos.android.employeeadmin.domain.UserRepository
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Department
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Role
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository, private val roleRepository: RoleRepository): ViewModel() {

    val users get() = userRepository.users

     suspend fun findAll() = withContext(Dispatchers.IO) {
         return@withContext userRepository.findAll()
     }

     suspend fun deleteById(id: Int): Int? = withContext(Dispatchers.IO) {
        return@withContext userRepository.deleteById(id)
    }

     suspend fun findById(id: Int): User? = withContext(Dispatchers.IO) {
        return@withContext userRepository.findById(id)
    }

     suspend fun save(user: User, roles: List<Role>?): User? = withContext(Dispatchers.IO) {
        return@withContext userRepository.save(user)?.also { user ->
            roles?.let { roleRepository.insertUserRoles(user.id, it) }
        }
    }

     suspend fun update(user: User, roles: List<Role>?): User? = withContext(Dispatchers.IO) {
        return@withContext userRepository.update(user)?.also {
            roles?.let { roleRepository.updateByUserId(user.id, it) }
        }
    }

     suspend fun findAllDepartments(): List<Department>? = withContext(Dispatchers.IO) {
        return@withContext userRepository.findAllDepartments()
    }

     suspend fun findAllRoles() = withContext(Dispatchers.IO) {
        return@withContext roleRepository.findAll()
    }

     suspend fun findRolesById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext roleRepository.findByUserId(id)
    }

}