package com.testfiles.ui.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RankingScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fileUri by viewModel.selectedFileUri.collectAsState()
    val ranking by viewModel.ranking.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var hasAutoSaved by remember { mutableStateOf(false) }

    // Efeito para salvar automaticamente o ranking original e a cópia
    LaunchedEffect(ranking, fileUri) {
        if (ranking.isNotEmpty() && fileUri != null && !hasAutoSaved) {
            // Cria a cópia "done_"
            viewModel.createDoneFileCopy(context, fileUri!!, ranking)
            hasAutoSaved = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            // Header
            CustomHeaderRanking(navController)

            Spacer(modifier = Modifier.height(8.dp))

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            // Message display
            message?.let { msg ->
                Text(
                    text = msg,
                    color = if (msg.startsWith("Erro")) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Ranking content
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "# hierarchylist",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (ranking.isEmpty()) {
                        Text(
                            text = "Nenhum resultado disponível",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        LazyColumn {
                            itemsIndexed(ranking) { index, (item, score) ->
                                Text(
                                    text = "${index + 1}. $item (${score}pts)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Divider()
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("home") { popUpTo("home") { inclusive = false } } },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text("Finalizar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderRanking(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton( onClick = {
                navController.navigate("home") { // Use a rota definida no seu NavGraph
                    popUpTo("home") { inclusive = false }
                }
            }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Home")
        }
        Text(
            text = "Ranking",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}