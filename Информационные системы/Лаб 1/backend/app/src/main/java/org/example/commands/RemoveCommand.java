package org.example.commands;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.requests.RemoveRequest;
import org.example.utils.cdi.LabWorkService;

@ApplicationScoped
public class RemoveCommand implements Command<RemoveRequest, Void> {

    @Inject
    private LabWorkService service;

    @Override
    public Class<RemoveRequest> requestType() {
        return RemoveRequest.class;
    }

    @Override
    public Void execute(RemoveRequest request) {
        service.delete(request.id());
        return null;
    }
}
