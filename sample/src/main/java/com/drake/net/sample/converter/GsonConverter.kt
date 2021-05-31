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

package com.drake.net.sample.converter

import com.drake.net.convert.NetConverter
import com.google.gson.GsonBuilder
import okhttp3.Response
import java.lang.reflect.Type

class GsonConverter : NetConverter {
    private val gson = GsonBuilder().serializeNulls().create()

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        return try {
            NetConverter.DEFAULT.onConvert<R>(succeed, response)
        } catch (e: Exception) {
            gson.fromJson(response.body()?.string(), succeed)
        }
    }
}