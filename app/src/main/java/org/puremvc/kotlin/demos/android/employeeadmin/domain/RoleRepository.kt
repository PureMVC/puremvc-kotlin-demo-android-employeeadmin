//
//  RoleRepository.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain

import org.json.JSONObject
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.RoleService
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Role
import javax.inject.Inject

class RoleRepository @Inject constructor(private val roleService: RoleService) {

    suspend fun findAll(): List<Role>? {
        val result = roleService.findAll()
        if (result.isSuccessful && result.body() != null) {
            return result.body()
        }
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun findByUserId(id: Int): List<Role>? {
        val result = roleService.findByUserId(id)
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun insertUserRoles(id: Int, roles: List<Role>): List<Int>? {
        val result = roleService.updateByUserId(id, roles.map { it.id })
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun updateByUserId(id: Int, roles: List<Role>): List<Int>? {
        val result = roleService.updateByUserId(id, roles.map { it.id })
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

}
