package com.testfiles.ui.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    // Carrega o conteúdo inicial do arquivo
    LaunchedEffect(fileUri) {
        isLoading = true
        fileContent = loadFileContent(context, fileUri)
        isLoading = false
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
            CustomHeaderEdit(navController)

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                // Campo de edição do arquivo
                TextField(
                    value = fileContent,
                    onValueChange = { fileContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    label = { Text("Conteúdo do Arquivo") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botões de ação
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botão Salvar
                    Button(
                        onClick = {
                            message = saveFileContent(context, fileUri, fileContent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Alterações")
                    }

                    // Botão Processar
                    Button(
                        onClick = {
                            viewModel.processData(fileContent)
                            navController.navigate("compare")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Process")
                    }
                }

                // Exibição de mensagens
                message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = if (it.startsWith("Erro")) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// Função auxiliar para carregar conteúdo do arquivo
private fun loadFileContent(context: Context, fileUri: Uri?): String {
    return try {
        fileUri?.let { uri ->
            context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader().readText()
            } ?: throw Exception("Não foi possível abrir o arquivo.")
        } ?: throw Exception("URI nulo")
    } catch (e: Exception) {
        "Erro: ${e.message}"
    }
}

// Função auxiliar para salvar conteúdo no arquivo
private fun saveFileContent(context: Context, fileUri: Uri?, content: String): String {
    return try {
        fileUri?.let { uri ->
            context.contentResolver.openOutputStream(uri, "wt")?.use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(content)
                    "Arquivo salvo com sucesso!"
                }
            } ?: "Erro: Não foi possível abrir o arquivo para escrita."
        } ?: "Erro: URI nulo"
    } catch (e: Exception) {
        "Erro ao salvar: ${e.message}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderEdit(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }
        Text(
            text = "Edit List",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}