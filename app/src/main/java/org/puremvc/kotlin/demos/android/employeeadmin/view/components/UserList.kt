//
//  UserList.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_list.*
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.Application
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListItemBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

interface IUserList {
    fun findAll(): ArrayList<User>?
    fun deleteById(id: Long?): Int?
}

class UserList: Fragment() {

    private var users: ArrayList<User>? = null

    private var delegate: IUserList? = null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as Application).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = UserListBinding.inflate(inflater, container, false).apply {
            fab.setOnClickListener { save() }

            ItemTouchHelper(object : SwipeHelper(recyclerView.context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteById(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(recyclerView)
        }

        savedInstanceState?.let { // User data: Cache
            val obj = it.getSerializable("users")
            if (obj is List<*>) {
                users = ArrayList(obj.filterIsInstance<User>())
            }
        }

        users?.let { // Cache hit
            binding.recyclerView.swapAdapter(UserListAdapter(it), false)
        } ?: run { // Cache miss
            IdlingResource.increment()
            lifecycleScope.launch(handler) {
                withContext(Dispatchers.IO) {
                    users = delegate?.findAll()
                }
            }.invokeOnCompletion {
                binding.recyclerView.swapAdapter(UserListAdapter(users ?: arrayListOf()), false)
                IdlingResource.decrement()
            }
        }

        return binding.root
    }

    private fun save() {
        navController.navigate(R.id.action_userList_to_userForm)
    }

    private fun deleteById(index: Int) {
        lifecycleScope.launch(handler) {
            withContext(Dispatchers.IO) {
                try {
                    delegate?.deleteById(users?.getOrNull(index)?.id)
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        recyclerView.adapter?.notifyDataSetChanged()
                        fault(null, exception) // throw exception
                    }
                }
            }
        }.invokeOnCompletion {
            users?.removeAt(index)
            recyclerView.adapter?.notifyItemRemoved(index)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<User>("user")?.observe(viewLifecycleOwner, Observer { user ->
            users?.forEachIndexed { index, _ ->
                if (users?.get(index)?.id == user.id) {
                    users?.set(index, user)
                    return@Observer
                }
            }
            users?.add(user)
            recyclerView.adapter?.notifyDataSetChanged()
        })
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putSerializable("users", users)
    }

    private fun fault(context: CoroutineContext?, exception: Throwable) {
        Log.d("UserList", "Error: $context ${exception.localizedMessage}")
        (activity as? EmployeeAdmin)?.fault(exception)
    }

    private val handler = CoroutineExceptionHandler { context, exception -> fault(context, exception) }

    fun setDelegate(delegate: IUserList) {
        this.delegate = delegate
    }

    private abstract class SwipeHelper(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        val background = ColorDrawable(Color.parseColor(context.getString(R.string.colorBackground)))
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp)!!

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

    }

    // Adapter
    private class UserListAdapter(val userVOs: ArrayList<User>): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

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

            fun bind(user: User) {
                userListItem.fullname = user.toString()
                userListItem.listener = View.OnClickListener {
                    navController.navigate(R.id.action_userList_to_userForm, bundleOf("id" to user.id))
                }
            }

        }
    }

}