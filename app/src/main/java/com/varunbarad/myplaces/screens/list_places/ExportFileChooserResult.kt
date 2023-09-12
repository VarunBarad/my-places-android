package com.varunbarad.myplaces.screens.list_places

import java.io.OutputStream

sealed class ExportFileChooserResult {
    object Error : ExportFileChooserResult()
    data class Success(val fileOutputStream: OutputStream): ExportFileChooserResult()
}
