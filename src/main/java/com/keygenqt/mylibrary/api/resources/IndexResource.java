package com.keygenqt.mylibrary.api.resources;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public class IndexResource extends RepresentationModel<EntityModel<IndexResource>> {

    private final String version;
    private final String description;
    private final String role;

    public IndexResource(String version, String description, String role) {
        this.version = version;
        this.description = description;
        this.role = role;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getRole() {
        return role;
    }
}
