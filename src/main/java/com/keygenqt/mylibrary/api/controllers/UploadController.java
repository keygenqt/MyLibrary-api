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

package com.keygenqt.mylibrary.api.controllers;

import com.keygenqt.mylibrary.base.BaseFormatResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
class UploadController {

    @Value("${images.dir}")
    private String dir;

    @Value("${images.url}")
    private String url;

    @PostMapping(path = "/upload-image")
    public ResponseEntity<Object> uploadImage(@RequestBody byte[] bytes) {
        var name = UUID.randomUUID() + ".png";
        try {
            FileUtils.writeByteArrayToFile(new File(dir + "/" + name), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseFormatResponse.getSuccessFormat(url + "/" + name);

    }

    @GetMapping(path = "/images/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String name) {
        var file = new File(dir + "/" + name);
        if (file.exists()) {
            try {
                return FileUtils.readFileToByteArray(new File(dir + "/" + name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
    }
}