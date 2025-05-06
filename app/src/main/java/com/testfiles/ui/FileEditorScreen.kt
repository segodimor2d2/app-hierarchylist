package com.testfiles.ui

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EditScreen(){
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
       FileEditorScreen()
   }
}

@Composable
fun FileEditorScreen() {
    val context = LocalContext.current
    var folderUri by remember { mutableStateOf<Uri?>(null) }
    var mdFiles by remember { mutableStateOf<List<Pair<String, Uri>>>(emptyList()) }
    var fileContent by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val folderPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let {
                folderUri = it
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                    it, DocumentsContract.getTreeDocumentId(it)
                )

                val files = mutableListOf<Pair<String, Uri>>()
                context.contentResolver.query(
                    childrenUri,
                    arrayOf(
                        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                        DocumentsContract.Document.COLUMN_DISPLAY_NAME
                    ),
                    null, null, null
                )?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val documentId = cursor.getString(0)
                        val displayName = cursor.getString(1)
                        if (displayName.endsWith(".md")) {
                            val fileUri = DocumentsContract.buildDocumentUriUsingTree(it, documentId)
                            files.add(displayName to fileUri)
                        }
                    }
                }
                mdFiles = files
            }
        }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { folderPicker.launch(null) }) {
            Text("Selecionar Pasta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mdFiles.isNotEmpty()) {
            Text("Arquivos Markdown encontrados:")
            Spacer(modifier = Modifier.height(8.dp))

            mdFiles.forEach { (name, uri) ->
                Text(
                    text = name,
                    modifier = Modifier
                        .clickable {
                            selectedFileUri = uri
                            context.contentResolver.openInputStream(uri)?.use { input ->
                                fileContent = input.bufferedReader().readText()
                            }
                        }
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedFileUri?.let {
            Text("Conteúdo do arquivo:")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = fileContent,
                onValueChange = { fileContent = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                context.contentResolver.openOutputStream(it, "wt")?.use { output ->
                    output.write(fileContent.toByteArray())
                }
            }) {
                Text("Salvar Alterações")
            }
        }
    }
}
