package itmo.programming.storage;

import itmo.programming.exceptions.DataAccessException;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.AuthenticationRequest;
import itmo.programming.responses.AuthenticationResponse;
import itmo.programming.storage.interaction.AddObject;
import itmo.programming.storage.interaction.ClearDb;
import itmo.programming.storage.interaction.DeleteObject;
import itmo.programming.storage.interaction.UpdateObjects;
import itmo.programming.storage.interaction.UserInteraction;
import java.sql.SQLException;

/**
 * The type Manage database.
 */
public class ManageDatabase {

    private final DeleteObject deleteObject;
    private final UserInteraction userInteraction;
    private final AddObject addObject;
    private final ClearDb clearDb;
    private final UpdateObjects updateObjects;

    /**
     * Instantiates a new Manage database.
     *
     * @param connectionDb the connection db
     */
    public ManageDatabase(ConnectionDb connectionDb) {
        this.userInteraction = new UserInteraction(connectionDb.getConnection());
        this.updateObjects = new UpdateObjects(connectionDb.getConnection(), userInteraction);
        this.deleteObject = new DeleteObject(connectionDb.getConnection(), userInteraction);
        this.addObject = new AddObject(connectionDb.getConnection(), userInteraction);
        this.clearDb = new ClearDb(connectionDb.getConnection(), userInteraction);
    }

    /**
     * Add.
     *
     * @param human the human
     * @param login the login
     */
    public HumanBeing add(HumanBeing human, String login) {
        try {
            return addObject.saveHuman(human, login);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Remove by index.
     *
     * @param id    the index
     * @param login the login
     */
    public boolean removeById(int id, String login) {
        try {
            deleteObject.removeById(id, login, getUserId(login));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Process authentication  response.
     *
     * @param request the request
     * @return the authentication response
     */
    public AuthenticationResponse processAuthentication(AuthenticationRequest request) {
        try {
            if (request.isRegistred()) {
                return userInteraction.checkUser(request.user(), request.hashPasswrd());
            } else {
                return userInteraction.registerUser(request.user(), request.hashPasswrd());
            }
        } catch (SQLException e) {
           return null;
        }

    }

    /**
     * Gets user index.
     *
     * @param login the login
     * @return the user index
     */
    public Integer getUserId(String login) {
        try {
            return userInteraction.getUserId(login);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Clear.
     *
     * @param login the login
     */
    public void clear(String login) {
        try {
            clearDb.clear(login);
        } catch (SQLException e) {
            return;
        }
    }

    /**
     * Update human being.
     *
     * @param human the human
     * @param id    the index
     * @param login the login
     */
    public HumanBeing update(HumanBeing human, int id, String login) {
        try {
            return updateObjects.update(human, id, login, getUserId(login));
        } catch (SQLException e) {
            return null;
        }
    }
}
