//
//  UserListFragment.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_list_fragment.*
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import kotlin.math.abs
import kotlin.math.roundToInt

class UserListFragment: Fragment(), GestureDetector.OnGestureListener {

    private lateinit var users: ArrayList<UserVO>

    private lateinit var adapter: UserListAdapter

    private lateinit var delegate: IUserListFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener { delegate.getDetails(null) }
    }

    fun setUsers(users: ArrayList<UserVO>) {
        this.users = users
        adapter = UserListAdapter(activity, users)
        listView.adapter = adapter

        val detector = GestureDetector(context, this)
        listView.setOnTouchListener { _: View?, event: MotionEvent? ->
            detector.onTouchEvent(event)
            false
        }
    }

    fun addUser(user: UserVO) {
       // adapter.notifyDataSetChanged();
    }

    fun updateUser(user: UserVO) {
        for (i in 0 until users.size) {
            if (users[i].username == user.username) {
                users[i] = user
                break
            }
        }
    }

    private class UserListAdapter(context: Context?, users: ArrayList<UserVO>?) : ArrayAdapter<UserVO>(context!!, 0, users as ArrayList<UserVO>) {
        override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, container, false)
            }
            (view?.findViewById<View>(android.R.id.text1) as TextView).text = getItem(position).toString()
            return view
        }
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val deltaX = e2.x - e1.x
        val deltaY = e2.y - e1.y
        try {
            val position = listView.pointToPosition(e1.x.roundToInt(), e1.y.roundToInt())
            val user = adapter.getItem(position)
            if (abs(deltaX) > abs(deltaY) && abs(deltaX) > 100 && abs(velocityX) > 100) { // Distance, Velocity Threshold
                if (deltaX <= 0) { // swipe left
                    AlertDialog.Builder(activity!!)
                        .setTitle("Delete")
                        .setMessage(String.format("Are you sure you want to delete %s?", user))
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes) { _: DialogInterface?, _: Int ->
                            users.remove(user)
                            adapter.notifyDataSetChanged()
                            delegate.delete(user!!)
                        }
                        .setNegativeButton(android.R.string.no, null)
                        .create().show()
                }
                return true
            }
        } catch (exception: Exception) {
        }
        return false
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        try {
            val user = adapter.getItem(listView.pointToPosition(e!!.x.roundToInt(), e.y.roundToInt()))
            delegate.getDetails(user)
            return true
        } catch (exception: Exception) {
        }
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean { return false }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean { return false }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onLongPress(e: MotionEvent?) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = activity as IUserListFragment
    }

    interface IUserListFragment {
        fun getDetails(userVO: UserVO?)
        fun delete(userVO: UserVO)
    }

}