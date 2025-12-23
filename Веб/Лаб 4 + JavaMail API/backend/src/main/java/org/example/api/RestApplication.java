package org.example.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public java.util.Set<Class<?>> getClasses() {
        return java.util.Set.of(
                org.example.exceptions.ApiExceptionMapper.class,
                org.example.resource.UserResource.class,
                org.example.resource.UserDataResource.class
        );
    }

}
