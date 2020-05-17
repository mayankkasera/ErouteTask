package com.example.eroutetask.ui.scan

import java.lang.StringBuilder

sealed class ScanState{
    data class ScanSuccess(var data : StringBuilder) : ScanState()
}