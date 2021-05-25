/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.request

import com.drake.net.interfaces.ProgressListener
import com.drake.net.tag.NetLabel
import com.drake.net.utils.lazyField
import okhttp3.*
import com.drake.net.compatible.*
import okio.ByteString
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

open class BodyRequest : BaseRequest() {

    open var body: RequestBody? = null
    open var partBody: MultipartBody.Builder by lazyField { MultipartBody.Builder() }
    open var formBody: FormBody.Builder by lazyField { FormBody.Builder() }
    open var mediaType: MediaType = MediaConst.FORM
    override var method = Method.POST

    //<editor-fold desc="Param">
    override fun param(name: String, value: String?, encoded: Boolean) {
        if (encoded) {
            formBody.addEncoded(name, value ?: return)
        } else formBody.add(name, value ?: return)
    }

    override fun param(name: String, value: Number?) {
        formBody.add(name, value.toString())
    }

    override fun param(name: String, value: Boolean?) {
        formBody.add(name, value.toString())
    }

    fun param(name: String, value: RequestBody?) {
        partBody.addFormDataPart(name, null, value ?: return)
    }

    fun param(name: String, value: ByteString?) {
        partBody.addFormDataPart(name, null, value?.toRequestBody() ?: return)
    }

    fun param(name: String, value: ByteArray?) {
        partBody.addFormDataPart(name, null, value?.toRequestBody() ?: return)
    }

    fun param(name: String, value: File?) {
        partBody.addFormDataPart(name, null, value?.asRequestBody() ?: return)
    }

    fun param(name: String, values: List<File?>?) {
        values?.forEach { value ->
            value?.asRequestBody()?.let { partBody.addFormDataPart(name, null, it) }
        }
    }

    fun param(name: String, fileName: String?, value: File?) {
        partBody.addFormDataPart(name, fileName, value?.asRequestBody() ?: return)
    }

    fun param(body: RequestBody, header: Headers? = null) {
        partBody.addPart(header, body)
    }

    fun param(body: MultipartBody.Part) {
        partBody.addPart(body)
    }

    //</editor-fold>

    //<editor-fold desc="JSON">
    fun json(body: JSONObject?) {
        this.body = body?.toString()?.toRequestBody(MediaConst.JSON)
    }

    fun json(body: JSONArray?) {
        this.body = body?.toString()?.toRequestBody(MediaConst.JSON)
    }

    fun json(body: String?) {
        this.body = body?.toRequestBody(MediaConst.JSON)
    }

    fun json(body: Map<String, Any?>?) {
        this.body = JSONObject(body ?: return).toString().toRequestBody(MediaConst.JSON)
    }

    fun json(vararg body: Pair<String, Any?>) {
        this.body = JSONObject(body.toMap()).toString().toRequestBody(MediaConst.JSON)
    }
    //</editor-fold>

    /**
     * 上传进度监听器
     */
    fun addUploadListener(progressListener: ProgressListener) {
        uploadListeners.add(progressListener)
    }

    private val uploadListeners = NetLabel.UploadListeners()

    override fun buildRequest(): Request {
        val body = if (body != null) body else {
            val form = formBody.build()
            try {
                partBody.build()
                for (i in 0 until form.size) {
                    val name = form.encodedName(i)
                    val value = form.encodedValue(i)
                    partBody.addFormDataPart(name, value)
                }
                partBody.setType(mediaType).build()
            } catch (e: IllegalStateException) {
                form
            }
        }

        return okHttpRequest.method(method.name, body)
            .url(httpUrl.build())
            .setLabel(tags)
            .setConverter(converter)
            .setLabel(downloadListeners)
            .setLabel(uploadListeners)
            .build()
    }
}