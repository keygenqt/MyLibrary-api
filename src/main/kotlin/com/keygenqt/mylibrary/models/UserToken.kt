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

package com.keygenqt.mylibrary.models

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "users_tokens")
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserToken(
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JsonIgnore
    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "token", nullable = false)
    var token: String = "",

    @Column(name = "uid", nullable = false)
    var uid: String = ""
)