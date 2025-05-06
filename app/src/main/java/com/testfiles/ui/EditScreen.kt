package com.testfiles.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EditScreen(uriString: String) {
    val context = LocalContext.current
    var fileContent by remember { mutableStateOf("") }
    val uri = remember(uriString) { Uri.parse(uriString) }

    LaunchedEffect(uriString) {
        context.contentResolver.openInputStream(uri)?.use { input ->
            fileContent = input.bufferedReader().readText()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editando Arquivo:")
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
            context.contentResolver.openOutputStream(uri, "wt")?.use { output ->
                output.write(fileContent.toByteArray())
            }
        }) {
            Text("Salvar Alterações")
        }
    }
}
