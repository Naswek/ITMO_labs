package org.example.commands;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entity.LabWork;
import org.example.requests.InfoRequest;
import org.example.utils.cdi.LabWorkService;

@ApplicationScoped
public class InfoCommand implements Command<InfoRequest, LabWork> {

    @Inject
    private LabWorkService service;

    @Override
    public Class<InfoRequest> requestType() {
        return InfoRequest.class;
    }

    @Override
    public LabWork execute(InfoRequest request) {
        return service.info(request.id());
    }
}
