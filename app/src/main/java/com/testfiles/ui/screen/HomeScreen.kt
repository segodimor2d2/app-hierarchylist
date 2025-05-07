package com.testfiles.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.util.PreferencesUtil
import com.testfiles.viewmodel.SharedViewModel
import com.testfiles.util.loadMdFiles
import androidx.documentfile.provider.DocumentFile
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    var folderUri by remember { mutableStateOf<Uri?>(null) }
    var mdFiles by remember { mutableStateOf<List<Pair<String, Uri>>>(emptyList()) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            if (folderUri == null) {
                Button(onClick = { folderPicker.launch(null) }) {
                    Text("Selecionar Pasta")
                }
            } else {
                CustomHeaderHome(navController)

                Spacer(modifier = Modifier.height(8.dp))

                mdFiles.forEach { (name, uri) ->
                    Text(
                        text = name,
                        modifier = Modifier
                            .clickable {
                                viewModel.selectFile(uri)
                                navController.navigate("edit")
                            }
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        folderUri?.let { folder ->
                            val formatter = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
                            val fileName = formatter.format(Date()) + ".md"

                            val docFolder = DocumentFile.fromTreeUri(context, folder)
                            val newFile = docFolder?.createFile("text/markdown", fileName)

                            newFile?.uri?.let { uri ->
                                context.contentResolver.openOutputStream(uri)?.use { output ->
                                    output.write("".toByteArray())
                                }

                                // Atualiza a lista localmente
                                mdFiles = mdFiles + (fileName to uri)

                                // Define o arquivo selecionado no ViewModel e navega
                                viewModel.selectFile(uri)
                                navController.navigate("edit")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Novo Arquivo")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderHome(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        IconButton({}) {
            Icon(Icons.Default.Home, contentDescription = "Voltar")
        }
        Text(
            text = "Files of Lists",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
