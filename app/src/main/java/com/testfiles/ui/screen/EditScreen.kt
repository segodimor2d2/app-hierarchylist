package com.testfiles.ui.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import java.io.OutputStreamWriter
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color // Para as cores customizadas, se necessário
import androidx.compose.material3.TextFieldDefaults


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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 136.dp) // 104dp botão + 48dp de padding
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { navController.popBackStack() }
                                .padding(vertical = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }

                        Text(
                            text = "Edit List",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    // Campo de texto para edição
                    TextField(
                        value = fileContent,
                        onValueChange = { fileContent = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        label = {
                            message?.let {
                                Text(
                                    text = it,
                                    color = if (it.startsWith("Erro")) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.primary
                                )
                            } ?: Text(
                                    text = "Digite algo",
                                    color = MaterialTheme.colorScheme.primary
                                )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    // Aqui passamos as variáveis necessárias
                    EditButtons(
                        context = context,
                        fileUri = fileUri,
                        fileContent = fileContent,
                        onSave = { message = it },
                        viewModel = viewModel,
                        navController = navController
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

@Composable
fun EditButtons(
    context: Context,
    fileUri: Uri?,
    fileContent: String,
    onSave: (String) -> Unit,
    viewModel: SharedViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botão Comparar
        Button(
            onClick = {
                val result = saveFileContent(context, fileUri, fileContent)
                onSave(result)
                viewModel.processData(fileContent)
                navController.navigate("compare")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "Comparar",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        //Spacer(modifier = Modifier.height(8.dp))

        // Botão Salvar Alterações
        Button(
            onClick = {
                val result = saveFileContent(context, fileUri, fileContent)
                onSave(result)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), // opcional (padrão do Material 3)
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary // cor do texto/ícone
            ),
        ) {
            Text(
                text = "Salvar Alterações",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
