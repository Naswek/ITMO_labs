package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Count less than soundtrack name request.
 */
public record CountLessThanSoundtrackNameRequest(String word) implements Serializable, Request {

}
