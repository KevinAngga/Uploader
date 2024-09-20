package com.angga.uploader.domain

data class ProgressUpdate(
    val bytesSent: Long,
    val totalBytes: Long
)
