package org.example.utils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Objects;
import org.example.commands.AddCommand;
import org.example.commands.Command;
import org.example.commands.InfoCommand;
import org.example.commands.RemoveCommand;
import org.example.commands.UpdateCommand;
import org.example.requests.CommandRequest;

@ApplicationScoped
public class CommandHandler {

    @Inject
    private AddCommand addCommand;

    @Inject
    private UpdateCommand updateCommand;

    @Inject
    private RemoveCommand removeCommand;

    @Inject
    private InfoCommand infoCommand;

    private Map<Class<?>, Command<?, ?>> commands;

    @PostConstruct
    private void registerCommands() {
        commands = Map.of(
                addCommand.requestType(), addCommand,
                updateCommand.requestType(), updateCommand,
                removeCommand.requestType(), removeCommand,
                infoCommand.requestType(), infoCommand
        );
    }

    public <R> R executeCommand(CommandRequest<R> request) {
        Objects.requireNonNull(request, "request");
        Command<?, ?> command = commands.get(request.getClass());
        if (command == null) {
            throw new IllegalArgumentException("Неизвестная команда: " + request.getClass().getSimpleName());
        }
        return dispatch(command, request);
    }

    @SuppressWarnings("unchecked")
    private static <R> R dispatch(Command<?, ?> command, CommandRequest<R> request) {
        Command<CommandRequest<R>, R> typed = (Command<CommandRequest<R>, R>) command;
        return typed.execute(request);
    }
}
