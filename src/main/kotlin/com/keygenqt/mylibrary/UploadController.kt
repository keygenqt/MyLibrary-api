/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary

import com.keygenqt.mylibrary.extensions.*
import org.apache.commons.io.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.bind.annotation.*
import java.io.*
import java.util.*
import javax.servlet.http.*

@Controller
class UploadController {

    @Value("\${images.path}")
    private val path: String = ""

    @PostMapping(path = ["/upload-image"])
    fun uploadImage(@RequestBody bytes: ByteArray, request: HttpServletRequest): ResponseEntity<Any> {
        val basePath = "${request.scheme}://${request.serverName}:${request.serverPort}"
        val name = "${UUID.randomUUID()}.png"
        FileUtils.writeByteArrayToFile(File("$path/$name"), bytes)
        return getSuccessFormat("$basePath/images/$name")
    }

    @GetMapping(path = ["/images/{name}"], produces = [MediaType.IMAGE_PNG_VALUE])
    @ResponseBody
    fun getImage(@PathVariable name: String): ByteArray {
        return FileUtils.readFileToByteArray(File("$path/$name"))
    }
}