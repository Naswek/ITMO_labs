package itmo.programming.storage.interaction;

/**
 * Represents foreign keys for related entities.
 */
public class ForeignKeys {

    private final Integer coordId;
    private final Integer carId;

    /**
     * Constructs a ForeignKeys instance with the specified coordinate and car IDs.
     *
     * @param coordId the coordinate ID
     * @param carId the car ID
     */
    public ForeignKeys(final Integer coordId, final Integer carId) {
        this.coordId = coordId;
        this.carId = carId;
    }

    /**
     * Returns the coordinate ID.
     *
     * @return the coordinate ID
     */
    public Integer getCoordId() {
        return coordId;
    }

    /**
     * Returns the car ID.
     *
     * @return the car ID
     */
    public Integer getCarId() {
        return carId;
    }
}
