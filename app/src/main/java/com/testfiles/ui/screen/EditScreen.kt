package com.testfiles.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import java.io.OutputStreamWriter

@Composable
fun EditScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fileUri by viewModel.selectedFileUri.collectAsState()

    var fileContent by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(fileUri) {
        isLoading = true
        try {
            fileUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { input ->
                    fileContent = input.bufferedReader().readText()
                } ?: run {
                    message = "Erro: não foi possível abrir o arquivo."
                }
            } ?: run {
                message = "URI nulo"
            }
        } catch (e: Exception) {
            message = "Exceção: ${e.message}"
        }
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.Red)
                .systemBarsPadding()
                .padding(16.dp)            ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                TextField(
                    value = fileContent,
                    onValueChange = { fileContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    label = { Text("Conteúdo do Arquivo") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    try {
                        fileUri?.let { uri ->
                            context.contentResolver.openOutputStream(uri)?.use { output ->
                                OutputStreamWriter(output).use { writer ->
                                    writer.write(fileContent)
                                    writer.flush()
                                }
                                message = "Arquivo salvo com sucesso!"
                            } ?: run {
                                message = "Erro: não foi possível abrir o arquivo para escrita."
                            }
                        }
                    } catch (e: Exception) {
                        message = "Erro ao salvar: ${e.message}"
                    }
                }) {
                    Text("Salvar Alterações")
                }

                message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }
    }
}
