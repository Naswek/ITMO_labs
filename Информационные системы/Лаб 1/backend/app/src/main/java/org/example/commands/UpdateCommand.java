package org.example.commands;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entity.LabWork;
import org.example.requests.UpdateRequest;
import org.example.utils.cdi.LabWorkService;

@ApplicationScoped
public class UpdateCommand implements Command<UpdateRequest, LabWork> {

    @Inject
    private LabWorkService service;

    @Override
    public Class<UpdateRequest> requestType() {
        return UpdateRequest.class;
    }

    @Override
    public LabWork execute(UpdateRequest request) {
        return service.update(request.id(), request.changes());
    }
}
