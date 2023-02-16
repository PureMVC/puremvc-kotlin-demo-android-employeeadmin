package org.puremvc.kotlin.demos.android.employeeadmin.model.service

data class Error(val code: String, val message: String) {

    override fun toString(): String {
        return "code: $code, message: $message"
    }

}
