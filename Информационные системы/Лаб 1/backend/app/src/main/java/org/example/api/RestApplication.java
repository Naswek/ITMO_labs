package org.example.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import org.example.resource.LabWorkResource;
import org.example.resource.ReferenceResource;
import org.example.resource.UserResource;

@ApplicationScoped
@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
                ApiExceptionMapper.class,
                LabWorkResource.class,
                UserResource.class,
                ReferenceResource.class
        );
    }
}
