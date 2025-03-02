package com.ralphevmanzano.catlibrary

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ralphevmanzano.catlibrary.presentation.cat_details.CatDetailsScreen
import com.ralphevmanzano.catlibrary.presentation.cat_list.CatListScreen
import com.ralphevmanzano.catlibrary.presentation.model.Screen
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val NOTIFICATIONS_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatLibraryTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATIONS_REQUEST_CODE)
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.CatList) {
        composable<Screen.CatList> {
            CatListScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToCatDetail = {
                    navController.navigate(
                        Screen.CatDetails(it.id, it.name)
                    )
                }
            )
        }
        composable<Screen.CatDetails> {
            CatDetailsScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}