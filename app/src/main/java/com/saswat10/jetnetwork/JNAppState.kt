package com.saswat10.jetnetwork

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Stable
class JNAppState(
    private val snackbarHostState: SnackbarHostState,
    val navController: NavController,
    private val snackbarManager: SnackbarManager,
    coroutineScope: CoroutineScope
){
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect {message ->
                snackbarHostState.showSnackbar(message)
                snackbarManager.clearSnackbarState()
            }
        }
    }

    fun popUp(){
        navController.popBackStack()
    }

    fun navigate(route: Any){
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: Any, popUp: Any){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo(popUp){inclusive = true}
        }
    }

    fun clearAndNavigate(route: Any){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo(0) {inclusive = true}
        }
    }

}





@Serializable
object Feed