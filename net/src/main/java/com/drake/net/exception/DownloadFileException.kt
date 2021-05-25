package com.drake.net.exception

import com.drake.net.compatible.*
import okhttp3.Response

/**
 * 下载文件异常
 */
class DownloadFileException(
    response: Response,
    info: String? = null,
    cause: Throwable? = null
) : NetException(response.request, info, cause)