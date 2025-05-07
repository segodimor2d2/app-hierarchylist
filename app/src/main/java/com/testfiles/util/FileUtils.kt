package com.testfiles.util

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun loadMdFiles(context: Context, folderUri: Uri, onFilesLoaded: (List<Pair<String, Uri>>) -> Unit) {
    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
        folderUri, DocumentsContract.getTreeDocumentId(folderUri)
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
                val fileUri = DocumentsContract.buildDocumentUriUsingTree(folderUri, documentId)
                files.add(displayName to fileUri)
            }
        }
    }

    onFilesLoaded(files)
}

fun createNewMarkdownFile(context: Context): File {
    val formatter = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
    val fileName = formatter.format(Date()) + ".md"
    val dir = File(context.filesDir, "markdown_files")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, fileName)
    file.writeText("# Novo Arquivo Markdown\n")
    return file
}