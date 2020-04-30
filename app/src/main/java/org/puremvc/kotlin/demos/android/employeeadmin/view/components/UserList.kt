//
//  UserList.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListItemBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.UserVO
import java.lang.ref.WeakReference

class UserList: Fragment() {

    private val userVOs by lazy { delegate.get()!!.getUsers() }

    private lateinit var navController: NavController

    private val delegate by lazy { WeakReference(activity as IUserList) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = UserListBinding.inflate(inflater, container, false).apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = UserListAdapter(userVOs)

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    val background = ColorDrawable(Color.parseColor(context.getString(R.string.colorBackground)))
                    val icon = ContextCompat.getDrawable(activity!!, R.drawable.ic_delete_white_24dp)!!

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        userVOs.removeAt(viewHolder.adapterPosition)
                        adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                    }

                    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                        val itemView = viewHolder.itemView

                        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        background.draw(canvas)

                        val margin = (itemView.height - icon.intrinsicHeight) / 2
                        val left = itemView.right - margin - icon.intrinsicWidth
                        val top = itemView.top + margin
                        val right = itemView.right - margin
                        val bottom = itemView.bottom - margin
                        icon.setBounds(left, top, right, bottom)
                        icon.draw(canvas)

                        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }

                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }
                }).attachToRecyclerView(this)
            }

            fab.setOnClickListener {
                navController.navigate(R.id.action_userList_to_userForm)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    // Adapter
    private class UserListAdapter(val userVOs: ArrayList<UserVO>): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return UserViewHolder(binding, parent.findNavController())
        }

        override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
            userViewHolder.bind(userVOs[position])
        }

        override fun getItemCount(): Int {
            return userVOs.size
        }

        // ViewHolder
        private class UserViewHolder(val userListItem: UserListItemBinding, val navController: NavController): RecyclerView.ViewHolder(userListItem.root) {

            fun bind(userVO: UserVO) {
                userListItem.fullname = userVO.toString()
                userListItem.listener = View.OnClickListener {
                    navController.navigate(R.id.action_userList_to_userForm, bundleOf("userVO" to userVO))
                }
            }

        }
    }

    interface IUserList {
        fun getUsers(): ArrayList<UserVO>
        fun delete(username: String)
    }

}