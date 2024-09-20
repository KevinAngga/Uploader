package com.angga.uploader.data

import android.net.Uri
import com.angga.uploader.domain.ProgressUpdate
import com.angga.uploader.domain.UploadRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.channelFlow

class UploadRepositoryImpl(
    private val httpClient: HttpClient,
    private val fileReader: FileReader
) : UploadRepository{
    override fun uploadFiles(contentUri: Uri, serverUrl: String) = channelFlow {
        val info = fileReader.uriToFileInfo(contentUri)
        println("===== server url "+serverUrl)
        httpClient.submitFormWithBinaryData(
            url = serverUrl,
            formData = formData {
                append("description", info.name)
                append("the_file", info.bytes, Headers.build {
                    append(HttpHeaders.ContentType, info.mimeType)
                    append(
                        HttpHeaders.ContentDisposition,
                        "filename=${info.name}"
                    )
                })
            }
        ) {
            onUpload { bytesSentTotal, totalBytes ->
                if (totalBytes > 0L) {
                    send(ProgressUpdate(bytesSentTotal, totalBytes))
                }
            }
        }
    }
}