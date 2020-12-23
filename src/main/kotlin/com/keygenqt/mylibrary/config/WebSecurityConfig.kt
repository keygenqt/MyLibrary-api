///*
// * Copyright 2020 Vitaliy Zarubin
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.keygenqt.mylibrary.security
//
//import com.keygenqt.mylibrary.models.*
//import com.keygenqt.mylibrary.models.User.Companion.AVATAR_HAPPY
//import com.keygenqt.mylibrary.models.User.Companion.AVATAR_TIRED
//import com.keygenqt.mylibrary.models.User.Companion.AVATAR_TOUCHED
//import com.keygenqt.mylibrary.models.repositories.*
//import org.springframework.beans.factory.annotation.*
//import org.springframework.context.annotation.*
//import org.springframework.http.*
//import org.springframework.http.HttpMethod.*
//import org.springframework.security.config.annotation.web.builders.*
//import org.springframework.security.config.annotation.web.configuration.*
//import org.springframework.security.crypto.bcrypt.*
//import org.springframework.security.crypto.password.*
//import org.springframework.security.web.authentication.*
//import javax.annotation.*
//
//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig : WebSecurityConfigurerAdapter() {
//
//    companion object {
//        const val SECRET_KEY = "8e5ONnewmseOnHvxEFuNxeUxeM9jvYWUKlkhdBdjkcDdDfu1azypNiN409y1QaDCFV"
//
//        const val ROLE_USER = "USER"
//        const val ROLE_ADMIN = "ADMIN"
//        const val ROLE_ANONYMOUS = "ANONYMOUS"
//    }
//
//    @Autowired
//    lateinit var repositoryUser: UserRepository
//
//    @Autowired
//    lateinit var repositoryUserToken: UserTokenRepository
//
//    @Bean fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//
//    @PostConstruct
//    fun addFirstUser() {
//        if (!repositoryUser.existsById(1)) {
//            repositoryUser.saveAll(
//                listOf(
//                    User(
//                        email = "dev@keygenqt.com",
//                        nickname = "Vitaliy Zarubin",
//                        password = "\$2a\$10\$Y8BcZWFswVyu2rBt9pD2JOXfIFMYPK/uzt5sriyeOjPWmRMuf8K42",
//                        role = ROLE_USER,
//                        avatar = AVATAR_TOUCHED,
//                        website = "https://keygenqt.com",
//                        location = "Volgodonsk, Russia",
//                        bio = "Most of all I want to become the kind of person who could make the world a little better."
//                    )
//                )
//            )
//        }
//    }
//
//    override fun configure(http: HttpSecurity) {
//        http
//            .csrf()
//            .disable()
//            .addFilterAfter(JWTAuthorizationFilter(repositoryUser, repositoryUserToken), UsernamePasswordAuthenticationFilter::class.java)
//            .authorizeRequests()
//            .antMatchers(GET, "/images/**").permitAll()
//            .antMatchers(GET, "/").permitAll()
//            .antMatchers(POST, "/login").permitAll()
//            .antMatchers(POST, "/join").permitAll()
//            .antMatchers(DELETE, "/books/**").permitAll()
//            .antMatchers(DELETE, "/**").hasAuthority(ROLE_ADMIN)
//            .anyRequest()
//            .authenticated()
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//    }
//}