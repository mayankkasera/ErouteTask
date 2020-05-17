package com.example.eroutetask.ui.scan

import java.lang.StringBuilder

sealed class ScanState{
    data class ScanSuccess(var data : StringBuffer) : ScanState()
    object NoData : ScanState()
}