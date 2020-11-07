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

package com.keygenqt.mylibrary.security

import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.http.HttpMethod.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.security.crypto.password.*
import org.springframework.security.web.authentication.*
import javax.annotation.*

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        const val SECRET_KEY = "8e5ONnewmseOnHvxEFuNxeUxeM9jvYWUKlkhdBdjkcDdDfu1azypNiN409y1QaDCFV"

        const val ROLE_USER = "USER"
        const val ROLE_ADMIN = "ADMIN"
    }

    @Autowired
    lateinit var repositoryUser: UserRepository

    @Autowired
    lateinit var repositoryUserToken: UserTokenRepository

    @Bean fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @PostConstruct
    fun addFirstUser() {
        if (repositoryUser.count() == 0L) {
            repositoryUser.save(User(
                image = "https://www.amazon.com/avatar/default/amzn1.account.AF53E22MXLWU7FIMB27VQXVQQPHQ?square=true&max_width=460",
                email = "admin@gmail.com",
                login = "admin",
                password = passwordEncoder().encode("12345"),
                role = ROLE_ADMIN)
            )
            repositoryUser.save(User(
                image = "https://www.amazon.com/avatar/default/amzn1.account.AGMEOBVVJKNYMOJRQUZVDMXXE5OA?square=true&max_width=460",
                email = "user@gmail.com",
                login = "user",
                password = passwordEncoder().encode("12345"),
                role = ROLE_USER)
            )
        }
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .addFilterAfter(JWTAuthorizationFilter(repositoryUser, repositoryUserToken), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(POST, "/login").permitAll()
            .antMatchers("/users/**").hasAuthority(ROLE_ADMIN)
            .antMatchers("/profile/users/**").hasAuthority(ROLE_ADMIN)
            .anyRequest().authenticated()
    }
}