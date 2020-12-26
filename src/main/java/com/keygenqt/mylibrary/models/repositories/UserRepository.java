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

package com.keygenqt.mylibrary.models.repositories;

import com.keygenqt.mylibrary.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query(value = "select u from User u where u.enabled=true and u.email=:email")
    User findAllByEmail(@Param("email") String email);

    @Override
    @Query(value = "select u from User u where u.enabled=true")
    Page<User> findAll(Pageable pageable);

    @Query(value = "select u from User u where u.enabled=true and u.id=:id")
    User findByIdActive(Long id);

    @RestResource(exported = false)
    User save(User s);
}
