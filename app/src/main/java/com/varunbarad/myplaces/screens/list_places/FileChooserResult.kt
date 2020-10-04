package com.varunbarad.myplaces.screens.list_places

import java.io.OutputStream

sealed class FileChooserResult {
    object Error : FileChooserResult()
    data class Success(val fileOutputStream: OutputStream): FileChooserResult()
}
