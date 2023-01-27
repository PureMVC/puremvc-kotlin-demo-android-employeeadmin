//
//  EmployeeAdmin.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.espresso.idling.CountingIdlingResource
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User

class EmployeeAdmin: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_admin)
        supportActionBar?.title = "Employee Admin"
    }

    fun alert(exception: Throwable): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(exception.javaClass.simpleName)
            .setMessage(exception.localizedMessage)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.okay, null)
            .create()
    }

}

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User> get() = _user

    fun setUser(user: User) {
        _user.value = user
    }

}

object IdlingResource {

    val counter = CountingIdlingResource("Espresso")

    fun increment() {
        counter.increment()
    }

    fun decrement() {
        if (!counter.isIdleNow) {
            counter.decrement()
        }
    }
}
