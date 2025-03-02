package com.ralphevmanzano.catlibrary.data.worker

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.data.remote.ImageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

/**
 * Worker class to download Cat images
 * This class is also responsible for showing a notification of download progress or completion
 */
class ImageDownloadWorker(
    private val context: Context,
    workerParams: WorkerParameters,
    private val imageService: ImageService
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_IMAGE_URL = "image_url"
        const val KEY_FILE_NAME = "file_name"
        const val PROGRESS = "Progress"
        const val DOWNLOAD_PROGRESS_NOTIFICATION_ID = 1
        const val CHANNEL_ID = "download_channel"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(0)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            safelySetForeground(createForegroundInfo(0))

            val imageUrl = inputData.getString(KEY_IMAGE_URL) ?: return@withContext Result.failure()
            val fileName = inputData.getString(KEY_FILE_NAME) ?: "downloaded_image.jpg"

            val response = imageService.downloadFile(imageUrl)
            val totalBytes = response.contentLength()

            val fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveFileWithMediaStore(fileName, response.byteStream(), totalBytes)
            } else {
                saveFile(fileName, response.byteStream(), totalBytes)
            }

            showCompletionNotification(fileName, fileUri)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun saveFileWithMediaStore(
        fileName: String,
        inputStream: InputStream,
        totalBytes: Long
    ): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = applicationContext.contentResolver
        val contentUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        contentUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytesRead: Int
                var downloadedBytes = 0L

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    downloadedBytes += bytesRead

                    // Calculate progress and update notification
                    val progress = ((downloadedBytes * 100) / totalBytes).toInt()
                    safelySetForeground(createForegroundInfo(progress))
                    setProgress(workDataOf(PROGRESS to progress))
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
        return contentUri ?: Uri.EMPTY
    }

    private suspend fun saveFile(
        fileName: String,
        inputStream: InputStream,
        totalBytes: Long
    ): Uri {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        val file = File(downloadDir, fileName)
        file.outputStream().use { output ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int
            var downloadedBytes = 0L

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead

                // Calculate progress and update notification
                val progress = ((downloadedBytes * 100) / totalBytes).toInt()
                safelySetForeground(createForegroundInfo(progress))
                setProgress(workDataOf(PROGRESS to progress))
            }
        }

        // Make the file visible in the Downloads app
        MediaScannerConnection.scanFile(
            applicationContext,
            arrayOf(file.absolutePath),
            arrayOf("image/*"),
            null
        )

        return Uri.fromFile(file)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Image Downloads",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows download progress for images"
        }
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun safelySetForeground(foregroundInfo: ForegroundInfo) {
        if (isAppInForeground()) {
            setForeground(foregroundInfo)
        }
    }

    /**
     * Create a foreground info for notification to show download progress
     */
    private fun createForegroundInfo(progress: Int): ForegroundInfo {
        val title = "Downloading image"

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .apply {
                    setContentTitle(title)
                    setSmallIcon(R.drawable.ic_download)
                    setOngoing(true)
                    setSilent(true)
                    setProgress(100, progress, false)
                    setContentText("$progress%")
                }

        return ForegroundInfo(DOWNLOAD_PROGRESS_NOTIFICATION_ID, notificationBuilder.build())
    }

    /**
     * Show a notification when the download is complete
     */
    private fun showCompletionNotification(fileName: String, fileUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.download_complete))
            .setContentText(fileName)
            .setSmallIcon(R.drawable.ic_download)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val uniqueNotificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(uniqueNotificationId, notification)
    }

    private fun isAppInForeground(): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        return appProcesses.any { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && it.processName == packageName }
    }
}