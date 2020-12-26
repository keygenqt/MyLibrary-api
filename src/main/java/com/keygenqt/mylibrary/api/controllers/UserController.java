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

import com.keygenqt.mylibrary.api.bodies.PasswordBody;
import com.keygenqt.mylibrary.api.bodies.UpdateUserBody;
import com.keygenqt.mylibrary.api.bodies.UpdateUserMessageToken;
import com.keygenqt.mylibrary.api.validators.PasswordValidator;
import com.keygenqt.mylibrary.api.validators.UpdateUserMessageTokenValidator;
import com.keygenqt.mylibrary.api.validators.UpdateUserValidator;
import com.keygenqt.mylibrary.base.BaseFormatResponse;
import com.keygenqt.mylibrary.base.JWTAuthorizationFilter;
import com.keygenqt.mylibrary.models.assemblers.UserAssembler;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final PasswordValidator passwordValidator;

    private final UpdateUserValidator updateUserValidator;

    private final UpdateUserMessageTokenValidator updateUserMessageTokenValidator;

    private final UserAssembler userAssembler;

    @Autowired
    public UserController(UserRepository userRepository,
                          UserTokenRepository userTokenRepository,
                          PasswordValidator passwordValidator,
                          UpdateUserValidator updateUserValidator,
                          UpdateUserMessageTokenValidator updateUserMessageTokenValidator,
                          UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.passwordValidator = passwordValidator;
        this.updateUserValidator = updateUserValidator;
        this.updateUserMessageTokenValidator = updateUserMessageTokenValidator;
        this.userAssembler = userAssembler;
    }

    @GetMapping(path = "/users/{id}")
    ResponseEntity<Object> userById(@PathVariable Long id) {
        var model = userRepository.findById(id).orElse(null);
        if (model != null) {
            return new ResponseEntity<>(userAssembler.toModel(model), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
    }

    @PutMapping(path = "/users/{id}")
    ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UpdateUserBody model, BindingResult bindingResult, HttpServletRequest request) {

        updateUserValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {
            var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));

            if (token == null || !token.getUserId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update user");
            }

            var user = userRepository.findById(id).orElse(null);

            if (user != null) {
                user.setAvatar(model.getAvatar());
                user.setNickname(model.getNickname());
                user.setWebsite(model.getWebsite());
                user.setLocation(model.getLocation());
                user.setBio(model.getBio());

                userRepository.save(user);

                return new ResponseEntity<>(userAssembler.toModel(user), HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update user");
    }

    @PostMapping(path = "/password")
    ResponseEntity<Object> password(@RequestBody PasswordBody model, HttpServletRequest request, BindingResult bindingResult) {

        passwordValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {

            var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));

            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update password");
            }

            var user = userRepository.findById(token.getUserId()).orElse(null);

            if (user != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(model.getPassword()));
                userRepository.save(user);
                return new ResponseEntity<>(userAssembler.toModel(user), HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update password");
    }

    @PutMapping(path = "/message-token")
    ResponseEntity<Object> messageToken(@RequestBody UpdateUserMessageToken model, HttpServletRequest request, BindingResult bindingResult) {

        updateUserMessageTokenValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {
            var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));
            if (token != null) {
                token.setMessageToken(model.getToken());
                userTokenRepository.save(token);
                return BaseFormatResponse.getSuccessFormat("Updated message token successfully");
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update message token");
    }
}
