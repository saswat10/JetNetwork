package com.saswat10.jetnetwork

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class JNViewModel: ViewModel() {
        fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
            viewModelScope.launch(
                CoroutineExceptionHandler { _, throwable ->
                    Log.d("ERROR", throwable.message.orEmpty())
                },
                block = block
            )

}