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

import com.drake.net.convert.JSONConvert
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class MoshiConverter : JSONConvert(code = "code", message = "msg", success = "0") {
    private val moshi = Moshi.Builder().build()

    override fun <R> String.parseBody(succeed: Type): R? {
        return moshi.adapter<R>(succeed).fromJson(this)
    }
}