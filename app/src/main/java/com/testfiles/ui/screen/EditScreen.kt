package com.testfiles.ui.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import java.io.OutputStreamWriter
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color // Para as cores customizadas, se necessário
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.ui.focus.onFocusChanged


@OptIn(ExperimentalLayoutApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun EditScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fileUri by viewModel.selectedFileUri.collectAsState()

    var fileContent by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }
    var showMethodHelp by rememberSaveable { mutableStateOf(false) }
    val varMetodo = viewModel.varMetodo
    val editorRequester = remember { BringIntoViewRequester() }
    var editorFocused by remember { mutableStateOf(false) }
    val keyboardVisible = WindowInsets.isImeVisible

    LaunchedEffect(keyboardVisible, editorFocused) {
        if (keyboardVisible && editorFocused) {
            editorRequester.bringIntoView()
        }
    }

    if (showMethodHelp) {
        AlertDialog(
            onDismissRequest = { showMethodHelp = false },
            title = { Text("Métodos de comparação") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Borda Modificado (MBC)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text =
                        "Método de Borda Modificado ou Matriz de Condorcet com pesos simplificados.\n\n" +
                        "Ele é uma variação de técnicas de votação ou hierarquização baseadas em comparações binárias ou ternárias.\n\n" +
                        "O método usa os valores -1, 0 e 1 para comparação par a par."
                    )
                    Text(
                        text = "Analytic Hierarchy Process - AHP",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text =
                        "Processo de Análise Hierárquica, é um método de tomada de decisão que compara alternativas duas a duas e calcula o peso ou a importância relativa de cada uma."
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodHelp = false }) {
                    Text("Fechar")
                }
            }
        )
    }

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
                .imePadding()
                .padding(horizontal = 16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }

                    // Edição: a tela pode rolar quando o teclado reduz o espaço disponível.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(editorRequester),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Edit List",
                            style = MaterialTheme.typography.titleLarge
                        )
                        TextField(
                            value = fileContent,
                            onValueChange = { fileContent = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(
                                    min = if (keyboardVisible) 120.dp else 160.dp,
                                    max = if (keyboardVisible) 120.dp else 280.dp
                                )
                                .onFocusChanged { editorFocused = it.isFocused },
                            label = { Text("Digite algo") },
                            supportingText = {
                                message?.let {
                                    Text(
                                        text = it,
                                        color = if (it.startsWith("Erro")) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
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
                        OutlinedButton(
                            onClick = {
                                message = saveFileContent(context, fileUri, fileContent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Salvar Alterações",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                    // Comparação: título, escolha do método e ação principal.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Compare List",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { showMethodHelp = true },
                                modifier = Modifier.semantics {
                                    contentDescription = "Explicação dos métodos AHP e MBC"
                                }
                            ) {
                                Text("?", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().selectableGroup(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf("ahp", "mbc").forEach { metodo ->
                                val isSelected = varMetodo == metodo
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                    else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outlineVariant
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .selectable(
                                                selected = isSelected,
                                                onClick = { viewModel.selecionarMetodo(metodo) },
                                                role = Role.RadioButton
                                            )
                                            .heightIn(min = 48.dp)
                                            .padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(selected = isSelected, onClick = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text(metodo.uppercase())
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = {
                                message = saveFileContent(context, fileUri, fileContent)
                                viewModel.processData(fileContent)
                                navController.navigate(if (viewModel.varMetodo == "ahp") "compare_ahp" else "compare")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Comparar - ${viewModel.varMetodo.uppercase()}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
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

