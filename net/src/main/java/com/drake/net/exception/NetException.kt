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

package com.drake.net.exception

import com.drake.net.compatible.*
import okhttp3.Request
import java.io.IOException


/**
 * 表示为Net发生的网络异常
 * 在转换器[com.drake.net.convert.NetConverter]中抛出的异常如果没有继承该类都会被视为数据转换异常[ConvertException], 该类一般用于自定义异常
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
open class NetException(
    open val request: Request,
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause) {

    var occurred: String = ""

    override fun getLocalizedMessage(): String? {
        return "${if (message == null) "" else message + " "}${request.url}$occurred"
    }
}