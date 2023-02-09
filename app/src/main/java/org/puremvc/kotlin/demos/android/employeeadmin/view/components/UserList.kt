//
//  UserList.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.view.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserList {
    suspend fun findAll(): LiveData<List<User>>?
    suspend fun deleteById(id: Long): Int?
}

class UserList: Fragment() {

    private var users: LiveData<List<User>>? = null

    private var _binding: UserListBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserList? = null

    companion object {
        const val TAG = "UserList"
    }

    init {
        ApplicationFacade.getInstance(ApplicationFacade.KEY).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserListBinding.inflate(inflater, container, false)

        binding.recyclerView.apply { // Set UI Data: Default
            adapter = Adapter()
            addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch(CoroutineExceptionHandler { _, e -> (activity as? EmployeeAdmin)?.alert(e)?.show() }) {
            IdlingResource.increment()
            launch { // Get User Data: IO
                users = delegate?.findAll()
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE
            users.let { // Set User Data
                (binding.recyclerView.adapter as Adapter).submitList(it?.value)
            }
            IdlingResource.decrement()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers

            (recyclerView.adapter as Adapter).listener = { user ->
                findNavController().navigate(R.id.action_userList_to_userForm, bundleOf("id" to user.id)) }

            fab.setOnClickListener { findNavController().navigate(R.id.action_userList_to_userForm) }

            ItemTouchHelper(object: SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewHolder as Adapter.ViewHolder).user?.let { deleteById(it.id) }
                }
            }).attachToRecyclerView(recyclerView)
        }

        users?.observe(viewLifecycleOwner) { it ->
            (binding.recyclerView.adapter as Adapter).submitList(it)
        }
    }

    private fun deleteById(id: Long) {
        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e).also {
                it?.setOnDismissListener { binding.recyclerView.adapter?.notifyItemChanged(id.toInt()) } // reset
            }?.show()
        }) {
            delegate?.deleteById(id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
         _binding = null
    }

    fun setDelegate(delegate: IUserList) {
        this.delegate = delegate
    }
}

private class Adapter(var listener: ((User) -> Unit)? = null): ListAdapter<User, Adapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class ViewHolder(view: View, val listener: ((User) -> Unit)? = null): RecyclerView.ViewHolder(view) {

        var user: User? = null
        val name: TextView

        init {
            name = view.findViewById(android.R.id.text1)
            view.setOnClickListener { user?.let { listener?.invoke(it) } }
        }

        fun bind(user: User) {
            this.user = user
            name.text = "%s %s".format(user.first, user.last)
        }
    }
}

object Diff : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id && oldItem.username.equals(newItem.username) &&
                oldItem.first.equals(newItem.first) && oldItem.last?.equals(newItem.last) == true
                && oldItem.email.equals(newItem.email) && oldItem.password.equals(newItem.password)
                && oldItem.department_id == newItem.department_id
    }

}
