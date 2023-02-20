//
//  UserService.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain.service

import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Department
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("/employees")
    @Headers("Accept: application/json")
    suspend fun findAll(): Response<List<User>>

    @GET("/employees/{id}")
    @Headers("Accept: application/json")
    suspend fun findById(@Path("id") id: Int): Response<User>

    @POST("/employees")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun save(@Body user: User): Response<User>

    @PUT("/employees/{id}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun update(@Path("id") id: Int, @Body user: User): Response<User>

    @DELETE("/employees/{id}")
    suspend fun deleteById(@Path("id") id: Int): Response<Int>

    @GET("/departments")
    @Headers("Accept: application/json", "Cache-Control: max-age=3600")
    suspend fun findAllDepartments(): Response<List<Department>>

}