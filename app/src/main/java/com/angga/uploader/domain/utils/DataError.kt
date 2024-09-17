package com.namea.domain.util

sealed interface DataError : Error {
    sealed class Network(val message: String? = null) : DataError {
        object REQUEST_TIMEOUT : Network("Request timed out")
        object UNAUTHORIZED : Network("Unauthorized access")
        object CONFLICT : Network("Conflict occurred")
        object TOO_MANY_REQUESTS : Network("Too many requests")
        object NO_INTERNET : Network("No internet connection")
        object PAYLOAD_TOO_LARGE : Network("Payload too large")
        object SERVER_ERROR : Network("Server error")
        object SERIALIZATION : Network("Serialization error")
        object UNKNOWN : Network("Unknown error")

        data class ApiError(val errorMessage: String) : Network(errorMessage)
    }

    enum class Camera : DataError {
        UNKNOWN,
        FILE_SIZE_LIMIT_REACHED,
        INSUFFICIENT_STORAGE,
        INVALID_OUTPUT_OPTIONS,
        ENCODING_FAILED,
        RECORDER_ERROR,
        NO_VALID_DATA,
        SOURCE_INACTIVE,
        ERROR_FIlE_IO,
        ERROR_CAPTURE_FAILED,
        ERROR_CAMERA_CLOSED,
        ERROR_INVALID_CAMERA
    }
}