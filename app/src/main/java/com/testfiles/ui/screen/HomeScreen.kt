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
    var showCreateFileButton by remember { mutableStateOf(true) }
    var showOpenFileButton by remember { mutableStateOf(true) }
    var showListFileButton by remember { mutableStateOf(false) }
    var showAccessFolderButton by remember { mutableStateOf(false) }

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

            if (showCreateFileButton) {
                Button( onClick = {
                    if (folderUri == null){
                        showCreateFileButton = false
                        showOpenFileButton = false
                        showAccessFolderButton  = true
                    } else {
                        showAccessFolderButton  = false
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
                    }
                }
                ) {
                    Text("Criar uma lista")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (showOpenFileButton) {
                Button(onClick = {
                    if (folderUri == null) {
                        showCreateFileButton = false
                        showOpenFileButton = false
                        showAccessFolderButton  = true
                    } else {
                        showListFileButton = true
                        showCreateFileButton = false
                        showOpenFileButton = false
                    }
                }) {
                    Text("Abrir uma lista")
                }
            }

            if (showAccessFolderButton) {
                Button( onClick = {
                    folderPicker.launch(null)
                    showOpenFileButton = true
                    showCreateFileButton = true
                    showAccessFolderButton  = false
                }) {
                    Text("Escolha uma pasta e de permissões")
                }
            }

            if (showListFileButton) {

                Row(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    IconButton(onClick = {
                        showOpenFileButton = true
                        showCreateFileButton = true
                        showListFileButton  = false
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                    Text(
                        text = "Listas",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

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
            }
        }
    }
}

