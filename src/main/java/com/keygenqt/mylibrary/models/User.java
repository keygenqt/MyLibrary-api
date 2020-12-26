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

package com.keygenqt.mylibrary.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public final static String AVATAR_HAPPY = "avatar_0";
    public final static String AVATAR_SURPRISED = "avatar_1";
    public final static String AVATAR_TIRED = "avatar_2";
    public final static String AVATAR_UPSET = "avatar_3";
    public final static String AVATAR_OVERWHELMED = "avatar_4";
    public final static String AVATAR_DEER = "avatar_5";
    public final static String AVATAR_ENAMORED = "avatar_6";
    public final static String AVATAR_BIRDIE = "avatar_7";
    public final static String AVATAR_WHAT = "avatar_8";
    public final static String AVATAR_SHOCKED = "avatar_9";
    public final static String AVATAR_TOUCHED = "avatar_10";
    public final static String AVATAR_ANGRY = "avatar_11";
    public final static String AVATAR_ZOMBIE = "avatar_12";
    public final static String AVATAR_PLAYFUL = "avatar_13";
    public final static String AVATAR_SLEEPY = "avatar_14";

    protected User() {}

    public User(String email, String nickname, String password, String role, String avatar) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
    }

    public User(String email, String nickname, String password, String role, String avatar, String website, String location, String bio) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.website = website;
        this.location = location;
        this.bio = bio;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "image")
    private String image;

    @Column(name = "website")
    private String website;

    @Column(name = "location")
    private String location;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @JsonIgnore
    @Column(name = "role", nullable = false)
    private String role;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token = "";

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserToken> tokens;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserToken> getTokens() {
        return tokens;
    }
}
