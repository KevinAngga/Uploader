import com.angga.uploader.R


fun CameraFlash.changeFlashIcon() : Int {
    return when(this) {
        CameraFlash.AUTO ->  R.drawable.flash_auto
        CameraFlash.FLASH_ON -> R.drawable.flash_on
        CameraFlash.FLASH_OFF ->  R.drawable.flash_off
    }
}


fun CameraUsage.setTitle() : Int {
    return when(this) {
        CameraUsage.RECORD -> R.string.blank
        else -> {
            R.string.take_photo
        }
    }
}

fun CameraUsage.setDescription() : Int {
    return when(this) {
        CameraUsage.RECORD -> R.string.blank
        CameraUsage.SELFIE -> R.string.take_selfie_desc
        CameraUsage.PHOTO -> R.string.take_id_card_desc
        CameraUsage.DOCUMENT -> R.string.blank
    }
}