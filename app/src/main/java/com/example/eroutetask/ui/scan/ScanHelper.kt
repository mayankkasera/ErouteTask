package com.example.eroutetask.ui.scan

import io.reactivex.Single
import java.util.regex.Matcher
import java.util.regex.Pattern

class ScanHelper {
    fun scan(input : StringBuilder) : Single<StringBuilder>{

        val pattern: Pattern = Pattern.compile("MW\\w+")

        val matcher: Matcher = pattern.matcher(input)
        val stringBuilder = StringBuilder()
        while (matcher.find()) {
            stringBuilder.append(matcher.group())
            stringBuilder.append("\n")
        }

        return Single.just(stringBuilder)
    }
}