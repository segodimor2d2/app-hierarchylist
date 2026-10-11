package com.testfiles.util

import android.content.Context
import android.net.Uri

object PreferencesUtil {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_FOLDER_URI = "folder_uri"
    private const val KEY_EDITOR_HEIGHT = "editor_height_dp"
    const val MIN_EDITOR_HEIGHT = 120f
    const val MAX_EDITOR_HEIGHT = 1000f

    fun getEditorHeight(context: Context): Float =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getFloat(KEY_EDITOR_HEIGHT, 240f)
            .coerceIn(MIN_EDITOR_HEIGHT, MAX_EDITOR_HEIGHT)

    fun saveEditorHeight(context: Context, height: Float) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putFloat(KEY_EDITOR_HEIGHT, height.coerceIn(MIN_EDITOR_HEIGHT, MAX_EDITOR_HEIGHT))
            .apply()
    }

    fun saveFolderUri(context: Context, uri: Uri) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_FOLDER_URI, uri.toString())
            .apply()
    }

    fun getSavedFolderUri(context: Context): Uri? {
        val uriString = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_FOLDER_URI, null)
        return uriString?.let { Uri.parse(it) }
    }
}
