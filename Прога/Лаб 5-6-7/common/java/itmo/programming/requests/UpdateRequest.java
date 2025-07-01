package itmo.programming.requests;

import itmo.programming.object.HumanBeing;
import java.io.Serializable;

/**
 * The type Update request.
 */
public record UpdateRequest(int id, HumanBeing human, String login) implements
        Serializable, Request  {

}

