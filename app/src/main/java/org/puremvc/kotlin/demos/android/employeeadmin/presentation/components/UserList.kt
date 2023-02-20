//
//  UserList.kt
//  PureMVC Android Demo - EmployeeAdmin
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package org.puremvc.kotlin.demos.android.employeeadmin.presentation.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.puremvc.kotlin.demos.android.employeeadmin.R
import org.puremvc.kotlin.demos.android.employeeadmin.databinding.UserListBinding
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.Resource
import org.puremvc.kotlin.demos.android.employeeadmin.domain.service.entity.User
import org.puremvc.kotlin.demos.android.employeeadmin.presentation.MainViewModel

@AndroidEntryPoint
class UserList: Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: UserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = UserListBinding.inflate(inflater, container, false)

        binding.apply { // Set UI Data: Default
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = UserAdapter { user ->
                findNavController().navigate(R.id.action_userList_to_userForm, bundleOf("id" to user.id))
            }
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }) {
            viewModel.findAll()
        }

        viewModel.users.observe(viewLifecycleOwner) { // Set User Data
            binding.progressBar.isVisible = false
            when(it) {
                is Resource.Loading -> binding.progressBar.isVisible = true
                is Resource.Success -> (binding.recyclerView.adapter as? UserAdapter)?.submitList(it.data)
                is Resource.Error -> Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { // Set Event Handlers
            fab.setOnClickListener { findNavController().navigate(R.id.action_userList_to_userForm) }

            ItemTouchHelper(object: SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewHolder as? UserAdapter.ViewHolder)?.let {
                        deleteByPosition(it.user, it.adapterPosition)
                    }
                }
                override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    val itemView = viewHolder.itemView

                    val background = ColorDrawable(Color.parseColor(context?.getString(R.string.colorBackground) ?: "#FF0000"))
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    background.draw(canvas)

                    context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_white_24dp) }?.let { icon ->
                        val margin = (itemView.height - icon.intrinsicHeight) / 2
                        icon.setBounds(itemView.right - margin - icon.intrinsicWidth, itemView.top + margin,
                            itemView.right - margin, itemView.bottom - margin)
                        icon.draw(canvas)
                    }

                    super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }).attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteByPosition(user: User, position: Int) {
        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            binding.recyclerView.adapter?.notifyItemChanged(position) // reset
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }) {
            viewModel.deleteById(user.id)
            viewModel.findAll()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
         _binding = null
    }

}

private class UserAdapter(var listener: (User) -> Unit): ListAdapter<User, UserAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }

    fun removeItem() {

    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        lateinit var user: User
        private val name: TextView = view.findViewById(android.R.id.text1)

        fun bind(user: User, listener: (User) -> Unit) {
            this.user = user
            name.text = "%s %s".format(user.first, user.last)
            view.setOnClickListener { listener(user) }
        }
    }

    class Diff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }

}

