package org.example.commands;

import org.example.requests.CommandRequest;

public interface Command<Q extends CommandRequest<R>, R> {
    Class<Q> requestType();

    R execute(Q request);
}
