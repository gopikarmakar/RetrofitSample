package com.hyend.mylibrary.extensions

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

internal fun MockWebServer.enqueueResponse(fileName: String, code: Int) {

    val inputStream = javaClass.classLoader?.getResourceAsStream("json-responses/$fileName")

    val source = inputStream?.let { inputStream.source().buffer() }
    source?.let {
        enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(source.readString(StandardCharsets.UTF_8))
        )
    }
}