//
//  RoleService.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain.service

import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Role
import retrofit2.Response
import retrofit2.http.*

interface RoleService {

    @GET("/roles")
    @Headers("Accept: application/json")
    suspend fun findAll(): Response<List<Role>>

    @GET("/employees/{id}/roles")
    @Headers("Accept: application/json")
    suspend fun findByUserId(@Path("id") id: Int): Response<List<Role>>

    @PUT("/employees/{id}/roles")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun updateByUserId(@Path("id") id: Int, @Body roles: List<Int>): Response<List<Int>>

}