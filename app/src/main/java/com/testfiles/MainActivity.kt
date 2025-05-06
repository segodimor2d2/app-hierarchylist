package com.testfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.testfiles.navigation.AppNavGraph
import com.testfiles.ui.theme.TestfilesTheme
import com.testfiles.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: SharedViewModel by viewModels()

        setContent {
            TestfilesTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
