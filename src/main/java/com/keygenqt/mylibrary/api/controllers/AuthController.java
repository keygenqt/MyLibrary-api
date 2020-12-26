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

import com.keygenqt.mylibrary.api.bodies.JoinBody;
import com.keygenqt.mylibrary.api.bodies.LoginBody;
import com.keygenqt.mylibrary.api.validators.JoinValidator;
import com.keygenqt.mylibrary.api.validators.LoginValidator;
import com.keygenqt.mylibrary.base.BaseFormatResponse;
import com.keygenqt.mylibrary.base.JWTAuthorizationFilter;
import com.keygenqt.mylibrary.config.WebSecurityConfig;
import com.keygenqt.mylibrary.models.User;
import com.keygenqt.mylibrary.models.UserToken;
import com.keygenqt.mylibrary.models.assemblers.UserAssembler;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
public class AuthController {

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final LoginValidator loginValidator;

    private final JoinValidator joinValidator;

    private final UserAssembler assembler;

    @Autowired
    public AuthController(UserRepository userRepository,
                          UserTokenRepository userTokenRepository,
                          LoginValidator loginValidator,
                          JoinValidator joinValidator,
                          UserAssembler assembler) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.loginValidator = loginValidator;
        this.joinValidator = joinValidator;
        this.assembler = assembler;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody LoginBody model, BindingResult bindingResult, HttpServletRequest request) {

        loginValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {
            var user = userRepository.findAllByEmail(model.getEmail());
            for (UserToken token : user.getTokens()) {
                if (token.getUid().equals(model.getUid())) {
                    token.setToken(JWTAuthorizationFilter.getJWTToken(user.getEmail(), user.getRole()));
                    userTokenRepository.save(token);
                    user.setToken(token.getToken());
                }
            }
            if (user.getToken().isEmpty()) {
                var token = new UserToken(
                        model.getUid(),
                        user.getId(),
                        JWTAuthorizationFilter.getJWTToken(user.getEmail(), user.getRole()),
                        request.getHeader(JWTAuthorizationFilter.ACCEPT_LANGUAGE)
                );
                userTokenRepository.save(token);
                user.setToken(token.getToken());
            }
            return new ResponseEntity<>(assembler.toModel(user), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/join")
    public ResponseEntity<Object> join(@RequestBody JoinBody model, BindingResult bindingResult, HttpServletRequest request) {

        joinValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {
            userRepository.save(new User(
                    model.getEmail(),
                    model.getNickname(),
                    new BCryptPasswordEncoder().encode(model.getPassword()),
                    WebSecurityConfig.ROLE_USER,
                    model.getAvatar()
            ));
            var user = userRepository.findAllByEmail(model.getEmail());
            var token = new UserToken(
                    model.getUid(),
                    user.getId(),
                    JWTAuthorizationFilter.getJWTToken(user.getEmail(), user.getRole()),
                    request.getHeader(JWTAuthorizationFilter.ACCEPT_LANGUAGE)
            );
            userTokenRepository.save(token);
            user.setToken(token.getToken());
            return new ResponseEntity<>(assembler.toModel(user), HttpStatus.OK);
        }
    }
}
