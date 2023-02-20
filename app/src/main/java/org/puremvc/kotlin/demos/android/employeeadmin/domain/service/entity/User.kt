//
//  User.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity

import android.os.Parcelable
import android.util.Patterns
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var username: String,
    var first: String,
    var last: String,
    var email: String,
    var password: String,
    var department: Department
): Parcelable {

    fun validate(confirm: String): Pair<Boolean, String> {
        return when {
            first.isBlank() || last.isBlank() || username.isBlank() || password.isBlank() || !department.validate() ->
                Pair(false, "Invalid Form Data.")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                Pair(false, "Invalid email")
            password != confirm ->
                Pair(false, "Your password and confirmation password do not match.")
            else ->
                Pair(true, "")
        }
    }

    override fun toString(): String {
        return "$last, $first"
    }

}

