package itmo.programming.enums;

/**
 * The enum Connections settings.
 */
public enum ConnectionsSettings {
    /**
     * Attempts connections settings.
     */
    ATTEMPTS(10),
    /**
     * Timeout connections settings.
     */
    TIMEOUT(5000),
    /**
     * Timesleep connections settings.
     */
    TIMESLEEP(100);

    ConnectionsSettings(int parameter) {
    }
}
