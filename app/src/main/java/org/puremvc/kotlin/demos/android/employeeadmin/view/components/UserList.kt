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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserList {
    fun findAll(): ArrayList<User>?
    fun deleteById(id: Long?): Int?
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

        IdlingResource.increment()
        val handler = CoroutineExceptionHandler { _, exception ->
            (activity as? EmployeeAdmin)?.alert(exception)?.show()
        }
        lifecycleScope.launch(handler) {
            savedInstanceState?.let { // Get User Data: State Restoration
                users = it.getParcelableArrayList("users", User::class.java)
            }

            users ?: run { // Get User Data: Network
                launch {
                    withContext(Dispatchers.IO) {
                        users = delegate?.findAll()
                    }
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with any UI Data thread
            binding.progressBar.visibility = View.GONE
            users = users ?: arrayListOf() // Default User Data
            binding.recyclerView.swapAdapter(Adapter(users ?: arrayListOf(), findNavController()), false) // Bind User Data

            binding.apply { // Bind Event Handlers
                fab.setOnClickListener {
                    findNavController().navigate(R.id.action_userList_to_userForm)
                }
                ItemTouchHelper(object : SwipeHelper(recyclerView.context) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        deleteById(viewHolder.adapterPosition)
                    }
                }).attachToRecyclerView(recyclerView)
            }
            IdlingResource.decrement()
        }

        return binding.root
    }

    private fun deleteById(index: Int) {
        val handler = CoroutineExceptionHandler { _, exception ->
            (activity as? EmployeeAdmin)?.alert(exception).also {
                it?.setOnDismissListener{ binding.recyclerView.adapter?.notifyItemChanged(index) }
            }?.show()
        }
        lifecycleScope.launch(handler) {
            withContext(Dispatchers.IO) {
                delegate?.deleteById(users?.get(index)?.id)
                withContext(Dispatchers.Main) {
                    users?.removeAt(index)
                    binding.recyclerView.adapter?.notifyItemRemoved(index)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            users?.forEachIndexed { index, _ ->
                if (users?.get(index)?.id == user.id) {
                    users?.set(index, user)
                    return@observe
                }
            }
            users?.add(user)
            binding.recyclerView.adapter?.notifyItemInserted(users?.count()?.minus(1) ?: 0);
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
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
                navController.navigate(R.id.action_userList_to_userForm, bundleOf("id" to users[position].id))
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