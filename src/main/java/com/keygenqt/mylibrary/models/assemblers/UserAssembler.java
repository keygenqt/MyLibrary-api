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

package com.keygenqt.mylibrary.models.assemblers;

import com.keygenqt.mylibrary.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.ArrayList;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    private final LinkRelationProvider relProvider;
    private final EntityLinks entityLinks;

    @Autowired
    public UserAssembler(LinkRelationProvider relProvider, EntityLinks entityLinks) {
        this.relProvider = relProvider;
        this.entityLinks = entityLinks;
    }

    @Override
    public EntityModel<User> toModel(User model) {
        ArrayList<Link> links = new ArrayList<>() {{
            add(entityLinks.linkToItemResource(User.class, model.getId()).withSelfRel());
            add(entityLinks.linkToItemResource(User.class, model.getId()).withRel(relProvider.getItemResourceRelFor(User.class)));
        }};
        for (Field value : User.class.getDeclaredFields()) {
            if (value.getAnnotation(OneToOne.class) != null) {
                links.add(entityLinks.linkToItemResource(User.class, model.getId() + "/" + value.getName()).withRel(value.getName()));
            }
        }
        return EntityModel.of(model, links);
    }
}
