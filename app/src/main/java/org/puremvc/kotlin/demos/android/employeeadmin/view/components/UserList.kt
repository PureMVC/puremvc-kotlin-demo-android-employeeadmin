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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserList {
    fun findAll(): ArrayList<User>?
    fun deleteByUsername(username: String)
}

class UserList: Fragment() {

    private var users: ArrayList<User>? = null

    private val viewModel: UserViewModel by activityViewModels()

    private var _binding: UserListBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).registerView(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserListBinding.inflate(inflater, container, false)

        savedInstanceState?.let { // Get User Data: Cache
            users = it.getParcelableArrayList("users", User::class.java)
        }

        users ?: run { // Get User Data: IO
            users = delegate?.findAll()
        }

        users = users ?: arrayListOf() // Set User Data: Default

        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.swapAdapter(Adapter(users!!, findNavController()), false) // Set User Data

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            fab.setOnClickListener { userForm() }
            ItemTouchHelper(object : SwipeHelper(recyclerView.context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteById(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(recyclerView)
        }

        viewModel.user.observe(viewLifecycleOwner) { user -> // Set User Data: View
            users?.forEachIndexed { index, _ ->
                if (users?.get(index)?.username == user.username) {
                    users?.set(index, user)
                    return@observe
                }
            }
            users?.add(user) // Set User Data
            binding.recyclerView.adapter?.notifyItemInserted(users?.count()?.minus(1) ?: 0);
        }
    }

    private fun userForm() {
        findNavController().navigate(R.id.action_userList_to_userForm) // Get User Data: View
    }

    private fun deleteById(index: Int) { // Delete User Data: IO
        delegate?.deleteByUsername(users?.get(index)?.username ?: "")
        binding.recyclerView.adapter?.notifyItemRemoved(index)
    }

    override fun onSaveInstanceState(bundle: Bundle) { // Set User Data: Cache
        super.onSaveInstanceState(bundle)
        bundle.putSerializable("users", users)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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

    private class Adapter(val users: ArrayList<User>, val navController: NavController): RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.name.text = "%s %s".format(users[position].first, users[position].last)
            viewHolder.name.setOnClickListener {
                navController.navigate(R.id.action_userList_to_userForm, bundleOf("username" to users[position].username))
            }
        }

        override fun getItemCount() = users.size

        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

            val name: TextView

            init {
                name = view.findViewById(android.R.id.text1)
            }
        }

    }

}