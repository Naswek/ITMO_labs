package itmo.programming.command;


import itmo.programming.requests.InfoRequest;
import itmo.programming.requests.Request;

/**
 * The type Info prompt.
 */
public class InfoPrompt implements Promptable {


    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new InfoRequest(login);
    }

}
