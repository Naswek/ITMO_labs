package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.Request;
import itmo.programming.requests.UserObjectsRequest;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;

/**
 * The type User objects command.
 */
public class UserObjectsCommand implements Command {

    private final ManageCollection manageCollection;
    private final ManageDatabase manageDatabase;

    /**
     * Instantiates a new User objects command.
     *
     * @param manageCollection the manage collection
     * @param manageDatabase   the manage database
     */
    public UserObjectsCommand(ManageCollection manageCollection, ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("user_objects");
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - выводит объекты пользователя, только их можно трогать";
    }

    @Override
    public MessageResponse execute(Request request) {
        final var userObjectsRequest = (UserObjectsRequest) request;
        final int id = manageDatabase.getUserId(userObjectsRequest.login());
        return new MessageResponse(manageCollection.showUsersObjects(id));
    }
}
