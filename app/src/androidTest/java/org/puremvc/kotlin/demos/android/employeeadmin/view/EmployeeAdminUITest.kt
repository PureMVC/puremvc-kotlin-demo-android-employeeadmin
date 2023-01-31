//
//  EmployeeAdminTest.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.EmployeeAdmin
import org.puremvc.kotlin.demos.android.employeeadmin.view.components.IdlingResource

@RunWith(AndroidJUnit4ClassRunner::class)
class EmployeeAdminUITest {

    @get: Rule
    val activityRule = ActivityScenarioRule(EmployeeAdmin::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResource.counter)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.counter)
    }

    @Test
    fun testList() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check { view, _ -> assertThat((view as RecyclerView).adapter?.itemCount, equalTo(3)) }
    }

    @Test
    fun testLarry() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.first)).check(matches(withText("Larry")))
        onView(withId(R.id.last)).check(matches(withText("Stooge")))
        onView(withId(R.id.email)).check(matches(withText("larry@stooges.com")))
        onView(withId(R.id.username)).check(matches(withText("lstooge")))
        onView(withId(R.id.username)).check(matches(not(isEnabled())))
        onView(withId(R.id.password)).check(matches(withText("ijk456")))
        onView(withId(R.id.confirm)).check(matches(withText("ijk456")))
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Accounting")))

        onView(withId(R.id.btnCancel)).perform(click())
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun updateCurly() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // update roles
        onView(withId(R.id.btnRoles)).perform(click())
        onView(withId(R.id.listView)).check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click())
        onView(withId(R.id.btnOk)).perform(click())

        // update details
        onView(withId(R.id.first)).perform(replaceText("Curly1"))
        onView(withId(R.id.last)).perform(replaceText("Stooge1"))
        onView(withId(R.id.email)).perform(replaceText("curly1@stooges.com"))
        onView(withId(R.id.username)).check(matches(not(isEnabled())))
        onView(withId(R.id.password)).perform(replaceText("abc123"))
        onView(withId(R.id.confirm)).perform(replaceText("abc123"))
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click()) // Accounting
        onView(withId(R.id.btnSave)).perform(click())

        // verify
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // verify roles
        onView(withId(R.id.btnRoles)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(isChecked()))
        onView(withId(R.id.btnCancel)).perform(click())

        // verify details
        onView(withId(R.id.first)).check(matches(withText("Curly1")))
        onView(withId(R.id.last)).check(matches(withText("Stooge1")))
        onView(withId(R.id.email)).check(matches(withText("curly1@stooges.com")))
        onView(withId(R.id.username)).check(matches(not(isEnabled())))
        onView(withId(R.id.password)).check(matches(withText("abc123")))
        onView(withId(R.id.confirm)).check(matches(withText("abc123")))
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Accounting"))))

        // revert roles
        onView(withId(R.id.btnRoles)).perform(click())
        onView(withId(R.id.listView)).check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click())
        onView(withId(R.id.btnOk)).perform(click())

        // revert details
        onView(withId(R.id.first)).perform(replaceText("Curly"))
        onView(withId(R.id.last)).perform(replaceText("Stooge"))
        onView(withId(R.id.email)).perform(replaceText("curly@stooges.com"))
        onView(withId(R.id.username)).check(matches(not(isEnabled())))
        onView(withId(R.id.password)).perform(replaceText("xyz987"))
        onView(withId(R.id.confirm)).perform(replaceText("xyz987"))
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(2).perform(click()) // Sales
        onView(withId(R.id.btnSave)).perform(click())
    }

    @Test
    fun testNewUser() {
        var total = 0
        onView(withId(R.id.recyclerView)).check { view, _ -> total = (view as RecyclerView).adapter?.itemCount!! } // total records
        onView(withId(R.id.fab)).perform(click()) // new user

        // select roles
        onView(withId(R.id.btnRoles)).perform(click())
        onView(withId(R.id.listView)).check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).perform(click())
        onView(withId(R.id.btnOk)).perform(click())

        onView(withId(R.id.btnRoles)).perform(click()) // click roles again
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(isChecked())) // verify the previous selection
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).check(matches(isChecked()))
        onView(withId(R.id.btnOk)).perform(click())

        // enter details
        onView(withId(R.id.first)).perform(replaceText("Joe"))
        onView(withId(R.id.last)).perform(replaceText("Stooge"))
        onView(withId(R.id.email)).perform(replaceText("joe@stooges.com"))
        onView(withId(R.id.username)).check(matches(isEnabled()))
        onView(withId(R.id.username)).perform(replaceText("jstooge"))
        onView(withId(R.id.password)).perform(replaceText("abc123"))
        onView(withId(R.id.confirm)).perform(replaceText("abc123"))
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(4).perform(click()) // +1, first entry is "--None Selected--"
        onView(withId(R.id.btnSave)).check(matches(withText("Save")))
        onView(withId(R.id.btnSave)).perform(click()) // save

        // user list view
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        // verify new record
        onView(withId(R.id.recyclerView)).check { view, _ -> assertThat((view as RecyclerView).adapter?.itemCount, equalTo(total + 1)) }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(total, click()))

        // verify roles
        onView(withId(R.id.btnRoles)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(isChecked()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).check(matches(isChecked()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(2).check(matches(not(isChecked())))
        Espresso.pressBack()

        // verify input data
        onView(withId(R.id.first)).check(matches(withText("Joe")))
        onView(withId(R.id.last)).check(matches(withText("Stooge")))
        onView(withId(R.id.email)).check(matches(withText("joe@stooges.com")))
        onView(withId(R.id.username)).check(matches(not(isEnabled())))
        onView(withId(R.id.username)).check(matches(withText("jstooge")))
        onView(withId(R.id.password)).check(matches(withText("abc123")))
        onView(withId(R.id.confirm)).check(matches(withText("abc123")))
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Shipping"))))
        onView(withId(R.id.btnSave)).check(matches(withText(R.string.update)))
        onView(withId(R.id.btnCancel)).perform(click())

        // user list view
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(total, swipeLeft()))
        onView(withId(R.id.recyclerView)).check { view, _ -> assertThat((view as RecyclerView).adapter?.itemCount, equalTo(total)) }
    }

    @Test
    fun testNewUserWithoutRoles() {
        var total = 0
        onView(withId(R.id.recyclerView)).check { view, _ -> total = (view as RecyclerView).adapter?.itemCount!! } // total records

        onView(withId(R.id.fab)).perform(click())

        // enter details
        onView(withId(R.id.first)).perform(replaceText("Shemp"))
        onView(withId(R.id.last)).perform(replaceText("Stooge"))
        onView(withId(R.id.email)).perform(replaceText("shemp@stooges.com"))
        onView(withId(R.id.username)).check(matches(isEnabled()))
        onView(withId(R.id.username)).perform(replaceText("sshemp"))
        onView(withId(R.id.password)).perform(replaceText("xyz987"))
        onView(withId(R.id.confirm)).perform(replaceText("xyz987"))
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnSave)).perform(click())

        // verify new record
        onView(withId(R.id.recyclerView)).check { view, _ -> assertThat((view as RecyclerView).adapter?.itemCount, equalTo(total + 1)) }
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(total, click()))

        // select roles
        onView(withId(R.id.btnRoles)).perform(click())
        onView(withId(R.id.listView)).check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click())
        onView(withId(R.id.btnOk)).perform(click())
        onView(withId(R.id.btnSave)).perform(click())

        // verify roles
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(total, click()))
        onView(withId(R.id.btnRoles)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(isChecked()))
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).check(matches(not((isChecked()))))

        onView(withId(R.id.btnOk)).perform(click())

        onView(withId(R.id.btnCancel)).perform(click())

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(total, swipeLeft()))
    }

    @Test
    fun testInvalidEntry() {
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.btnSave)).perform(click())

        onView(withText("Invalid Form Data.")).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun testInvalidPassword() {
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.first)).perform(replaceText("Shemp"))
        onView(withId(R.id.last)).perform(replaceText("Stooge"))
        onView(withId(R.id.email)).perform(replaceText("shemp@stooges.com"))
        onView(withId(R.id.username)).perform(replaceText("sshemp"))
        onView(withId(R.id.password)).perform(replaceText("abc123"))
        onView(withId(R.id.confirm)).perform(replaceText("ijk456"))
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.btnSave)).perform(click())

        onView(withText("Your password and confirmation password do not match.")).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
    }

}