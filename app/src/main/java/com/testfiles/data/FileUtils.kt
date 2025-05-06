import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract

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
