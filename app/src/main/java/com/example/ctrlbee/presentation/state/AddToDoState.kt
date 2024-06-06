//package com.example.ctrlbee.presentation.state
//
//sealed class AddToDoState {
//    data object Loading: AddToDoState()
//    data class Failed(
//        val message: String
//    ): AddToDoState()
//    data class Success(
//        val message: String
//    ): AddToDoState()
//}
package com.example.ctrlbee.presentation.state

sealed class AddToDoState {
    data object Initial: AddToDoState()
    data object Loading: AddToDoState()
    data class Failed(
        val message: String
    ): AddToDoState()
    data object Success: AddToDoState()
}

