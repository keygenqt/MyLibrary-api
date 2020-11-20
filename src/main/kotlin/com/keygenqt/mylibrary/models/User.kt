package com.keygenqt.mylibrary.models

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

import com.fasterxml.jackson.annotation.*
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.*

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", nullable = true)
    var email: String = "",

    @Column(name = "nickname", nullable = true)
    var nickname: String = "",

    @Column(name = "image")
    var image: String = "",

    @Column(name = "website")
    var website: String = "",

    @Column(name = "location")
    var location: String = "",

    @Column(name = "bio")
    var bio: String = "",

    @Column(name = "avatar", nullable = true)
    var avatar: String = "avatar_0",

    @JsonIgnore
    @Column(name = "password", nullable = true)
    var password: String = "",

    @JsonIgnore
    @Column(name = "enabled", nullable = true)
    var enabled: Boolean = true,

    @JsonIgnore
    @Column(name = "role", nullable = true)
    var role: String = "USER",

    @Transient
    var token: String = "",

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var tokens: List<UserToken> = listOf()
) {
    companion object {
        const val AVATAR_HAPPY = "avatar_0"
        const val AVATAR_SURPRISED = "avatar_1"
        const val AVATAR_TIRED = "avatar_2"
        const val AVATAR_UPSET = "avatar_3"
        const val AVATAR_OVERWHELMED = "avatar_4"
        const val AVATAR_DEER = "avatar_5"
        const val AVATAR_ENAMORED = "avatar_6"
        const val AVATAR_BIRDIE = "avatar_7"
        const val AVATAR_WHAT = "avatar_8"
        const val AVATAR_SHOCKED = "avatar_9"
        const val AVATAR_TOUCHED = "avatar_10"
        const val AVATAR_ANGRY = "avatar_11"
        const val AVATAR_ZOMBIE = "avatar_12"
        const val AVATAR_PLAYFUL = "avatar_13"
        const val AVATAR_SLEEPY = "avatar_14"
    }
}