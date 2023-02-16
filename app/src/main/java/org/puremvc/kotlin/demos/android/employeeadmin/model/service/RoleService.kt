package org.puremvc.kotlin.demos.android.employeeadmin.model.service

import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.Role
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