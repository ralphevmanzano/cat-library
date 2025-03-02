package com.ralphevmanzano.catlibrary.domain.model.networking

sealed class DownloadStatus {
    data object Idle : DownloadStatus()
    data class Downloading(val progress: Int) : DownloadStatus()
    data class Success(val fileName: String) : DownloadStatus()
    data class Error(val message: String) : DownloadStatus()
}