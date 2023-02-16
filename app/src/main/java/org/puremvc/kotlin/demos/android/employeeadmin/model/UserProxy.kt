//
//  UserProxy.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.model

import okhttp3.ResponseBody
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.Error
import org.puremvc.kotlin.demos.android.employeeadmin.model.service.UserService
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Department
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import org.puremvc.kotlin.multicore.patterns.proxy.Proxy
import retrofit2.Converter

class UserProxy(private val userService: UserService, val converter: Converter<ResponseBody, Error>): Proxy(NAME, null) {

    companion object {
        const val NAME = "UserProxy"
    }

    suspend fun findAll(): List<User>? {
        val result = userService.findAll()
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun findById(id: Int): User? {
        val result = userService.findById(id)
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun save(user: User): User? {
        val result = userService.save(user)
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun update(user: User): User? {
        val result = userService.update(user.id, user)
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun deleteById(id: Int): Int? {
        val result = userService.deleteById(id)
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

    suspend fun findAllDepartments(): List<Department>? {
        val result = userService.findAllDepartments()
        if (result.isSuccessful)
            return result.body()
        throw Exception(result.errorBody()?.let { converter.convert(it).toString() })
    }

}