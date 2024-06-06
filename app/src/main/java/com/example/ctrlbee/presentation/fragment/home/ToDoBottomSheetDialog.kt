//package com.example.ctrlbee.presentation.fragment.home
//
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.fragment.app.viewModels
//import by.kirich1409.viewbindingdelegate.viewBinding
//import com.example.ctrlbee.R
//import com.example.ctrlbee.core.Constants.Companion.TO_DO_LIST_DATE
//import com.example.ctrlbee.data.repository.SharedPreferencesRepo
//import com.example.ctrlbee.databinding.ItemBottomSheetAddNoteBinding
//import com.example.ctrlbee.presentation.state.AddToDoState
//import com.example.ctrlbee.presentation.viewmodel.ToDoListViewModel
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class ToDoBottomSheetDialog : BottomSheetDialogFragment(R.layout.item_bottom_sheet_add_note) {
//
//    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
//    private val viewBinding: ItemBottomSheetAddNoteBinding by viewBinding()
//    private val viewModel: ToDoListViewModel by viewModels()
//
//    private lateinit var date: String
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initActions()
//        initObserver()
//
//        date = requireArguments().getString(TO_DO_LIST_DATE).toString()
//    }
//
//    private fun initActions() = with(viewBinding) {
//        uploadButton.setOnClickListener {
//            val toDoText = fieldToDo.text.toString()
//
//            if (toDoText.isEmpty()) {
//                Toast.makeText(requireContext(), "Fill to do field..", Toast.LENGTH_LONG).show()
//            } else {
//                viewModel.addToDo(
//                    sharedPreferencesRepo.getUserRefreshToken(),
//                    toDoText,
//                    date
//                )
//            }
//        }
//    }
//
//    private fun initObserver() {
//        viewModel.addToDoStateLiveData.observe(viewLifecycleOwner, ::handleState)
//    }
//
//    private fun handleState(state: AddToDoState) {
//        when (state) {
//            is AddToDoState.Failed -> {
//                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
//            }
//            is AddToDoState.Loading -> {}
//            is AddToDoState.Success -> {
//                val result = state.message
//                if (result == "CONTINUE") {
//                    Toast.makeText(
//                        requireContext(),
//                        "To do added",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }
//                dialog?.dismiss()
//            }
//        }
//    }
//}
package com.example.ctrlbee.presentation.fragment.home

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants.Companion.TO_DO_LIST_DATE
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.ItemBottomSheetAddNoteBinding
import com.example.ctrlbee.presentation.state.AddToDoState
import com.example.ctrlbee.presentation.viewmodel.ToDoListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToDoBottomSheetDialog : BottomSheetDialogFragment(R.layout.item_bottom_sheet_add_note) {

    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: ItemBottomSheetAddNoteBinding by viewBinding()
    private val viewModel: ToDoListViewModel by viewModels()

    private lateinit var date: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
        initObserver()

        date = requireArguments().getString(TO_DO_LIST_DATE).toString()
    }

    private fun initActions() = with(viewBinding) {
        uploadButton.setOnClickListener {
            val toDoText = fieldToDo.text.toString()

            if (toDoText.isEmpty()) {
                Toast.makeText(requireContext(), "Fill to do field..", Toast.LENGTH_LONG).show()
            } else {
                viewModel.addToDo(
                    sharedPreferencesRepo.getUserRefreshToken(),
                    toDoText,
                    date
                )
            }
        }
    }

    private fun initObserver() {
        viewModel.addToDoStateLiveData.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: AddToDoState) {
        when (state) {
            is AddToDoState.Failed -> {
                Log.d("MyLog", "result: failed")
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
            is AddToDoState.Loading -> {}
            is AddToDoState.Success -> {
                //val result = state.message
                Log.d("MyLog", "result: success")
                //if (result == "CONTINUE") {
                Toast.makeText(
                    requireContext(),
                    "To do added",
                    Toast.LENGTH_SHORT,
                ).show()
                //}
                this.dismiss()
                viewModel.refreshAddToDoState()


            }

            AddToDoState.Initial -> {
            }
        }
    }

    interface OnDialogDismissListener {
        fun onDialogDismissed()
    }

    var dismissListener: OnDialogDismissListener? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDialogDismissed()
    }
}