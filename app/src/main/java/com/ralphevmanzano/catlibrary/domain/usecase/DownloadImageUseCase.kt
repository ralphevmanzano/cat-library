package com.ralphevmanzano.catlibrary.domain.usecase

import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloaderRepository
import kotlinx.coroutines.flow.Flow

class DownloadImageUseCase(private val imageDownloaderRepository: ImageDownloaderRepository) {
    operator fun invoke(imageUrl: String, fileName: String): Flow<DownloadStatus> {
        return imageDownloaderRepository.downloadImage(imageUrl, fileName)
    }
}