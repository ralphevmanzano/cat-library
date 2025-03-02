package com.ralphevmanzano.catlibrary.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ralphevmanzano.catlibrary.data.worker.ImageDownloadWorker
import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloaderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ImageDownloaderRepositoryImpl(
    private val workManager: WorkManager,
    private val context: Context
) : ImageDownloaderRepository {

    override fun downloadImage(imageUrl: String, fileName: String): Flow<DownloadStatus> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return flow { emit(DownloadStatus.Error("Storage permission required")) }
        }

        val workData = workDataOf(
            ImageDownloadWorker.KEY_IMAGE_URL to imageUrl,
            ImageDownloadWorker.KEY_FILE_NAME to fileName
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val downloadRequest = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
            .setInputData(workData)
            .setConstraints(constraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            "image_download_${fileName.replace(" ", "_")}",
            ExistingWorkPolicy.REPLACE,
            downloadRequest
        )

        return workManager.getWorkInfoByIdFlow(downloadRequest.id)
            .map { workInfo ->
                val progress = workInfo?.progress?.getInt(ImageDownloadWorker.PROGRESS, 0) ?: 0
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> DownloadStatus.Success(fileName)
                    WorkInfo.State.FAILED -> DownloadStatus.Error("Download failed")
                    WorkInfo.State.RUNNING -> DownloadStatus.Downloading(progress)
                    else -> null
                }
            }
            .filterNotNull()
    }
}