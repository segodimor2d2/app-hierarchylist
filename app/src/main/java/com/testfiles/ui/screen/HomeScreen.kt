package com.testfiles.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import com.testfiles.util.PreferencesUtil
import com.testfiles.util.loadMdFiles
import com.testfiles.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    var folderUri by remember { mutableStateOf<Uri?>(null) }
    var folderName by remember { mutableStateOf("Listas") }
    var mdFiles by remember { mutableStateOf<List<Pair<String, Uri>>>(emptyList()) }
    var showOpenFileButton by remember { mutableStateOf(true) }

    LaunchedEffect(folderUri) {
        folderName = withContext(Dispatchers.IO) {
            folderUri?.let { uri ->
                runCatching { DocumentFile.fromTreeUri(context, uri)?.name }
                    .getOrNull()
                    ?.takeIf { it.isNotBlank() }
            } ?: "Listas"
        }
    }

    val folderPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let {
                PreferencesUtil.saveFolderUri(context, it)
                folderUri = it
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                loadMdFiles(context, it) { files ->
                    mdFiles = files
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        val savedUri = PreferencesUtil.getSavedFolderUri(context)
        savedUri?.let {
            folderUri = it
            loadMdFiles(context, it) { files ->
                mdFiles = files
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            if (folderUri == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SolicitarAcessoCard {
                        folderPicker.launch(null)
                        showOpenFileButton = true
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                    .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (mdFiles.isEmpty()) 16.dp else 84.dp)
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
                                    contentDescription = "Voltar",
                                    modifier = Modifier
                                )
                            }

                            Text(
                                text = folderName,
                                style = MaterialTheme.typography.titleLarge,
                            )

                        }

                        if (mdFiles.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "Você ainda não possui uma lista",
                                        style = MaterialTheme.typography.headlineSmall,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Para iniciar, clique em ‘criar uma lista’",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    CreateListButton(
                                        folderUri = folderUri,
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        } else {
                            val (completedFiles, listFiles) = mdFiles.partition { it.first.startsWith("done_") }
                            FileSection(
                                title = "Listas",
                                emptyMessage = "Nenhuma lista para comparar.",
                                files = listFiles,
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                navController = navController,
                                viewModel = viewModel
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                            FileSection(
                                title = "Listas concluídas",
                                emptyMessage = "Nenhuma lista concluída.",
                                files = completedFiles,
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                navController = navController,
                                viewModel = viewModel
                            )
                        }

                    }

                    if (mdFiles.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            CreateListButton(
                                folderUri = folderUri,
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitarAcessoCard(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Bem vindo!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Para iniciar a hierarquização e organização da lista, escolha uma pasta para salvá-la. Em seguida, clique em 'Permitir Acesso'.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onContinue) {
                    Text(
                        text = "Continuar",
                    )
                }
            }
        }
    }
}



@Composable
private fun FileSection(
    title: String,
    emptyMessage: String,
    files: List<Pair<String, Uri>>,
    modifier: Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (files.isEmpty()) {
            Text(
                emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FileGridView(files, navController, viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileGridView(
    mdFiles: List<Pair<String, Uri>>,
    navController: NavController,
    viewModel: SharedViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6), // ou use Adaptive(100.dp)
        //columns = GridCells.Adaptive(minSize = 96.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(mdFiles) { (name, uri) ->
            val isDone = name.startsWith("done_")
            val icon = if (isDone) Icons.Default.Done else Icons.Filled.List
            val iconColor = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

            Column(
                modifier = Modifier
                    .width(72.dp)
                    .clickable {
                        viewModel.selectFile(uri)
                        navController.navigate("edit")
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = iconColor,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)

                )
            }
        }
    }
}

@Composable
fun CreateListButton(
    folderUri: Uri?,
    navController: NavController,
    viewModel: SharedViewModel
) {
    Button(
        onClick = {
            if (folderUri != null) {
                viewModel.startNewList()
                navController.navigate("edit")
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = "Criar uma lista",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
