//
//  UserRepository.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.UserService
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Department
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Resource
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users: LiveData<Resource<List<User>>> get() = _users

    suspend fun findAll() {
        _users.postValue(Resource.Loading())
        val result = userService.findAll()
        if (result.isSuccessful && result.body() != null)
            _users.postValue(Resource.Success(result.body()!!))
        else
            result.errorBody()?.let { _users.postValue(Resource.Error(JSONObject(it.charStream().readText()).toString())) }
                ?: _users.postValue(Resource.Error("Unknown Error"))
    }

    suspend fun findById(id: Int): User? {
        val result = userService.findById(id)
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun save(user: User): User? {
        val result = userService.save(user)
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun update(user: User): User? {
        val result = userService.update(user.id, user)
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun deleteById(id: Int): Int? {
        val result = userService.deleteById(id)
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

    suspend fun findAllDepartments(): List<Department>? {
        val result = userService.findAllDepartments()
        if (result.isSuccessful)
            return result.body()
        result.errorBody()?.let {
            throw Exception(JSONObject(it.string()).getString("message"))
        } ?: throw Exception("Unknown Error")
    }

}