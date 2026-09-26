package org.example.commands;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entity.LabWork;
import org.example.requests.AddRequest;
import org.example.utils.cdi.LabWorkService;

@ApplicationScoped
public class AddCommand implements Command<AddRequest, LabWork> {

    @Inject
    private LabWorkService service;

    @Override
    public Class<AddRequest> requestType() {
        return AddRequest.class;
    }

    @Override
    public LabWork execute(AddRequest request) {
        return service.add(request.labWork());
    }
}
