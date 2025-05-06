package com.testfiles.util

import android.content.Context
import android.net.Uri

object PreferencesUtil {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_FOLDER_URI = "folder_uri"

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
