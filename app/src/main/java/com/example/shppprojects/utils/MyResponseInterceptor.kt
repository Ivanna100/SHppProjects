package com.example.shppprojects.utils

import com.example.shppprojects.utils.ext.log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MyResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val contentType = response.body?.contentType()
        val bodyString = response.body?.string() ?: ""

        val braceCount = bodyString.count {it == '{'|| it == '}'}
        val isBracketError = braceCount % 2 != 0
        val newBodyString = if(isBracketError) "$bodyString}".also { catch() } else bodyString

        val body = newBodyString.toResponseBody(contentType)
        return response.newBuilder().body(body).build()
    }

    companion object {
        private val catch =
            { log("=============== !!! CATCH } SHREDINGERA !!! ===============") }
    }
}