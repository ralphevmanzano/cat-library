package com.ralphevmanzano.catlibrary.domain.usecase

import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloader
import kotlinx.coroutines.flow.Flow

class DownloadImageUseCase(private val imageDownloader: ImageDownloader) {
    operator fun invoke(imageUrl: String, fileName: String): Flow<DownloadStatus> {
        return imageDownloader.downloadImage(imageUrl, fileName)
    }
}