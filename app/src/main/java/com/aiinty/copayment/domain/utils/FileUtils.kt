package com.aiinty.copayment.domain.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    fun uriToFile(prefix: String, suffix: String, uri: Uri, context: Context): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile(prefix, suffix, context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return tempFile
    }
    fun generateTimeStampFileName(suffix: String): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "$timestamp$suffix"
    }
}