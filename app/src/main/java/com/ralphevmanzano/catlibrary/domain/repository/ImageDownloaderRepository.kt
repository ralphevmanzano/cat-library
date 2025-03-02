package com.ralphevmanzano.catlibrary.domain.repository

import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface ImageDownloaderRepository {
    fun downloadImage(imageUrl: String, fileName: String): Flow<DownloadStatus>
}