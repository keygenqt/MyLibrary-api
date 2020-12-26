package com.keygenqt.mylibrary.api.assemblers;

import com.keygenqt.mylibrary.api.resources.IndexResource;
import com.keygenqt.mylibrary.config.WebSecurityConfig;
import com.keygenqt.mylibrary.models.Book;
import com.keygenqt.mylibrary.models.Genre;
import com.keygenqt.mylibrary.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;

@Service
public class IndexResourceAssembler {

    final static String API_KEY_MESSAGE_TOKEN = "message-token";
    final static String API_KEY_UPLOAD_IMAGE = "upload-image";
    final static String API_KEY_LOGIN = "login";
    final static String API_KEY_JOIN = "join";
    final static String API_KEY_PASSWORD = "password";
    final static String API_KEY_PROFILE = "profile";

    private final LinkRelationProvider relProvider;

    private final EntityLinks entityLinks;

    @Autowired
    public IndexResourceAssembler(LinkRelationProvider relProvider, EntityLinks entityLinks) {
        this.relProvider = relProvider;
        this.entityLinks = entityLinks;
    }

    IndexResource buildIndex() {
        var role = WebSecurityConfig.ROLE_ANONYMOUS;
        for (GrantedAuthority item : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (item.getAuthority() != null) {
                role = item.getAuthority();
            }
        }

        var links = new ArrayList<Link>() {{
            add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/login").build().toUriString(), API_KEY_LOGIN));
            add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/join").build().toUriString(), API_KEY_JOIN));
            add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/password").build().toUriString(), API_KEY_PASSWORD));
        }};

        if (role.equals(WebSecurityConfig.ROLE_USER) || role.equals(WebSecurityConfig.ROLE_ADMIN)) {

            links.add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/message-token").build().toUriString(), API_KEY_MESSAGE_TOKEN));
            links.add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/upload-image").build().toUriString(), API_KEY_UPLOAD_IMAGE));
            links.add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/genres/search/findAll{?language,page,size,sort}").build().toUriString())
                    .withRel(relProvider.getCollectionResourceRelFor(Genre.class)));

            links.add(entityLinks.linkToCollectionResource(Book.class)
                    .withRel(relProvider.getCollectionResourceRelFor(Book.class)));
            links.add(entityLinks.linkToCollectionResource(User.class)
                    .withRel(relProvider.getCollectionResourceRelFor(User.class)));
        }

        if (role.equals(WebSecurityConfig.ROLE_ADMIN)) {
            links.add(Link.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/profile").build().toUriString(), API_KEY_PROFILE));
        }

        var resource = new IndexResource("1.0.0", "HATEOAS API for app MyLibrary", role.replace("ROLE_", ""));
        resource.add(links);
        return resource;
    }
}
