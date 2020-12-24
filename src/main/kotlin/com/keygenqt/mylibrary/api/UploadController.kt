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

package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.base.BaseFormatResponse
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.io.File
import java.util.*

@Controller
class UploadController {

    @Value("\${images.dir}")
    private val dir: String = ""

    @Value("\${images.url}")
    private val url: String = ""

    @PostMapping(path = ["/upload-image"])
    fun uploadImage(@RequestBody bytes: ByteArray): ResponseEntity<Any> {
        val name = "${UUID.randomUUID()}.png"
        FileUtils.writeByteArrayToFile(File("$dir/$name"), bytes)
        return BaseFormatResponse.getSuccessFormat("$url/$name")

    }

    @GetMapping(path = ["/images/{name}"], produces = [MediaType.IMAGE_PNG_VALUE])
    @ResponseBody
    fun getImage(@PathVariable name: String): ByteArray {
        val file = File("$dir/$name")
        if (file.exists()) {
            return FileUtils.readFileToByteArray(File("$dir/$name"))
        }
        throw ResponseStatusException(NOT_FOUND, "Image not found")
    }
}