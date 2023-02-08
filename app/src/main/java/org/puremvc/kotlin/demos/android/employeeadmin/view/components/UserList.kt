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
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.puremvc.kotlin.demos.android.employeeadmin.ApplicationFacade
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.model.valueObject.User
import java.lang.ref.WeakReference

interface IUserList {
    fun findAll(): List<User>?
    fun findAllUsers(): LiveData<List<User>>?
    fun deleteById(id: Long): Int?
}

class UserList: Fragment() {

    private var users: ArrayList<User>? = null

    private val viewModel: UserViewModel by activityViewModels()

    private var _binding: UserListBinding? = null

    private val binding get() = _binding!!

    private var delegate: IUserList? = null

    companion object {
        const val TAG = "UserList"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationFacade.getInstance(ApplicationFacade.KEY).register(WeakReference(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserListBinding.inflate(inflater, container, false)

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e)?.show()
        }) {
            IdlingResource.increment()

            binding.recyclerView.adapter = Adapter(arrayListOf()) // Set UI Data: Default

            savedInstanceState?.let { // Get User Data: Cache
                users = it.getParcelableArrayList("users", User::class.java)
            }

            launch { // Get User Data: IO
                withContext(Dispatchers.IO) {
                    users ?: run {
                        users = delegate?.findAll()?.let { ArrayList(it) }
                    }
                }
            }
        }.invokeOnCompletion { // Upon completion to avoid race condition with UI Data thread
            binding.progressBar.visibility = View.GONE

            users?.let { // Set User Data
                (binding.recyclerView.adapter as Adapter).users = it
                binding.recyclerView.adapter?.notifyItemRangeChanged(0, it.size)
            }

            IdlingResource.decrement()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            (recyclerView.adapter as Adapter).listener = OnItemClickListener { _, _, position, _ ->
                findNavController().navigate(R.id.action_userList_to_userForm, bundleOf("id" to users?.get(position)?.id)) }

            fab.setOnClickListener { findNavController().navigate(R.id.action_userList_to_userForm) }
            ItemTouchHelper(object: SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteById(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(recyclerView)
        }

        viewModel.user.observe(viewLifecycleOwner) { user -> // Set User Data: View Result
            users?.forEachIndexed { index, _ ->
                if (users?.get(index)?.id == user.id) {
                    users?.set(index, user)
                    return@observe
                }
            }
            users?.add(user) // Set User Data
            binding.recyclerView.adapter?.notifyItemInserted(users?.count()?.minus(1) ?: 0)
        }
    }

    private fun deleteById(index: Int) {
        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            (activity as? EmployeeAdmin)?.alert(e).also {
                it?.setOnDismissListener { binding.recyclerView.adapter?.notifyItemChanged(index) } // reset
            }?.show()
        }) {
            withContext(Dispatchers.IO) {
                delegate?.deleteById(users?.get(index)?.id ?: 0)
            }
        }.invokeOnCompletion {
            users?.removeAt(index)
            binding.recyclerView.adapter?.notifyItemRemoved(index)
        }
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

    private class Adapter(var users: ArrayList<User>, var listener: OnItemClickListener? = null): RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view, listener)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.name.text = "%s %s".format(users[position].first, users[position].last)
        }

        override fun getItemCount() = users.size

        class ViewHolder(view: View, val listener: OnItemClickListener? = null): RecyclerView.ViewHolder(view) {

            val name: TextView

            init {
                name = view.findViewById(android.R.id.text1)
                view.setOnClickListener {
                    listener?.onItemClick(null, view, adapterPosition, itemId)
                }
            }
        }
    }

}