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

package com.keygenqt.mylibrary.config;

import com.keygenqt.mylibrary.models.User;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final static String SECRET_KEY = "8e5ONnewmseOnHvxEFuNxeUxeM9jvYWUKlkhdBdjkcDdDfu1azypNiN409y1QaDCFV";

    public final static String ROLE_USER = "USER";
    public final static String ROLE_ADMIN = "ADMIN";
    public final static String ROLE_ANONYMOUS = "ANONYMOUS";

    private final UserRepository repositoryUser;
    private final UserTokenRepository repositoryUserToken;

    @Autowired
    public WebSecurityConfig(UserRepository repositoryUser, UserTokenRepository repositoryUserToken) {
        this.repositoryUser = repositoryUser;
        this.repositoryUserToken = repositoryUserToken;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .addFilterAfter(new JWTAuthorizationFilter(repositoryUser, repositoryUserToken), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/join").permitAll()
                .antMatchers(HttpMethod.DELETE, "/books/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").hasAuthority(ROLE_ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @PostConstruct
    void addFirstUser() {
        if (!repositoryUser.existsById(1L)) {
            repositoryUser.saveAll(new ArrayList<>() {{
                add(new User(
                        "dev@keygenqt.com",
                        "Vitaliy Zarubin",
                        "$2a$10$Y8BcZWFswVyu2rBt9pD2JOXfIFMYPK/uzt5sriyeOjPWmRMuf8K42",
                        ROLE_USER,
                        User.AVATAR_TOUCHED,
                        "https://keygenqt.com",
                        "Volgodonsk, Russia",
                        "Most of all I want to become the kind of person who could make the world a little better."
                ));
            }});
        }
    }
}
