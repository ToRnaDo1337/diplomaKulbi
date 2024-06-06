//package com.example.ctrlbee.presentation.fragment.home
//
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import by.kirich1409.viewbindingdelegate.viewBinding
//import com.example.ctrlbee.R
//import com.example.ctrlbee.core.Constants.Companion.TO_DO_LIST_DATE
//import com.example.ctrlbee.data.repository.SharedPreferencesRepo
//import com.example.ctrlbee.databinding.FragmentToDoListBinding
//import com.example.ctrlbee.presentation.state.ToDoListState
//import com.example.ctrlbee.presentation.viewmodel.ToDoListViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class ToDoListFragment: Fragment(R.layout.fragment_to_do_list) {
//
//    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
//    private val viewBinding: FragmentToDoListBinding by viewBinding()
//    private val viewModel: ToDoListViewModel by viewModels()
//
//    private val toDoListAdapter by lazy(LazyThreadSafetyMode.NONE) {
//        ToDoListAdapter()
//    }
//
//    private lateinit var date: String
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        date = requireArguments().getString(TO_DO_LIST_DATE).toString()
//
//        initActions()
//        initObservers()
//
//        viewModel.getToDoList(
//            token = sharedPreferencesRepo.getUserRefreshToken(),
//            time = date
//        )
//    }
//
//    private fun initActions() = with(viewBinding) {
//        toDoListRecycler.adapter = toDoListAdapter
//        toDoListRecycler.layoutManager = LinearLayoutManager(context)
//
//        textViewDate.text = date
//
//        fab.setOnClickListener {
//            val bottomSheet = ToDoBottomSheetDialog()
//            bottomSheet.arguments = Bundle().apply {
//                putString(TO_DO_LIST_DATE, date)
//            }
//            bottomSheet.show(parentFragmentManager, "MyBottomSheetTag")
//        }
//
//        refreshLayoutToDoList.setOnRefreshListener {
//            viewModel.getToDoList(
//                token = sharedPreferencesRepo.getUserRefreshToken(),
//                time = date
//            )
//        }
//    }
//
//    private fun initObservers() {
//        viewModel.toDoListState.observe(viewLifecycleOwner, ::handleToDoListState)
//    }
//
//    private fun handleToDoListState(state: ToDoListState) = with(viewBinding) {
//        when (state) {
//            is ToDoListState.Failed -> {
//                Log.e("ToDoListFragmentTag", state.message)
//            }
//            is ToDoListState.Loading -> {}
//            is ToDoListState.Success -> {
//                val tasks = state.result
//                toDoListAdapter.submitList(tasks)
//                refreshLayoutToDoList.isRefreshing = false
//            }
//        }
//    }
//
//}
package com.example.ctrlbee.presentation.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants.Companion.TO_DO_LIST_DATE
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentToDoListBinding
import com.example.ctrlbee.presentation.state.ToDoListState
import com.example.ctrlbee.presentation.viewmodel.ToDoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToDoListFragment: Fragment(R.layout.fragment_to_do_list), ToDoBottomSheetDialog.OnDialogDismissListener {

    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentToDoListBinding by viewBinding()
    private val viewModel: ToDoListViewModel by viewModels()

    private val toDoListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ToDoListAdapter()
    }

    private lateinit var date: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = requireArguments().getString(TO_DO_LIST_DATE).toString()

        viewModel.getToDoList(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            time = date
        )

        initActions()
        initObservers()


    }

    private fun initActions() = with(viewBinding) {
        toDoListRecycler.adapter = toDoListAdapter
        toDoListRecycler.layoutManager = LinearLayoutManager(context)

        textViewDate.text = date

        backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        fab.setOnClickListener {
            val bottomSheet = ToDoBottomSheetDialog()
            bottomSheet.dismissListener = this@ToDoListFragment
            bottomSheet.arguments = Bundle().apply {
                putString(TO_DO_LIST_DATE, date)
            }


            bottomSheet.show(parentFragmentManager, "MyBottomSheetTag")

        }
        //Log.d("MyLog", "Dialog on dismiss listener")

        refreshLayoutToDoList.setOnRefreshListener {
            viewModel.getToDoList(
                token = sharedPreferencesRepo.getUserRefreshToken(),
                time = date
            )
        }
    }

    private fun initObservers() {
        viewModel.toDoListState.observe(viewLifecycleOwner, ::handleToDoListState)
    }

    private fun handleToDoListState(state: ToDoListState) = with(viewBinding) {
        when (state) {
            is ToDoListState.Failed -> {
                Log.e("ToDoListFragmentTag", state.message)
            }
            is ToDoListState.Loading -> {}
            is ToDoListState.Success -> {
                val tasks = state.result
                toDoListAdapter.submitList(tasks)
                refreshLayoutToDoList.isRefreshing = false
            }
        }
    }

    override fun onDialogDismissed() {
        //initObservers()
        Log.d("MyLog", "On dialog dismissed")
        viewModel.getToDoList(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            time = date
        )

    }
}