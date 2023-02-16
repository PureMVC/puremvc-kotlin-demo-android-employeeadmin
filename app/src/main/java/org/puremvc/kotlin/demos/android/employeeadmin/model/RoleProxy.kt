//
//  RoleProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import okhttp3.ResponseBody
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.Error
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.RoleService
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import retrofit2.Converter

class RoleProxy(private val roleService: RoleService, val converter: Converter<ResponseBody, Error>): Proxy(NAME, null) {

    companion object {
        const val NAME: String = "RoleProxy"
    }

    suspend fun findAll(): List<Role>? {
        val result = roleService.findAll()
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun findByUserId(id: Int): List<Role>? {
        val result = roleService.findByUserId(id)
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun insertUserRoles(id: Int, roles: List<Role>): List<Int>? {
        val result = roleService.updateByUserId(id, roles.map { it.id })
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun updateByUserId(id: Int, roles: List<Role>): List<Int>? {
        val result = roleService.updateByUserId(id, roles.map { it.id })
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

}
