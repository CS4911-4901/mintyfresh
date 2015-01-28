package edu.gatech.cs4911.mintyfresh.queryresponse;

/**
 * A Building holds information about a nearby building's name, id, latitude, and longitude.
 */
public class Building extends DBResponseObject {
    /**
     * The human-readable name of this Building.
     */
    private String name;
    /**
     * A three-character ID assigned to this Building.
     */
    private String id;
    /**
     * The latitude of this Building.<br />
     * latitude > 0 = N<br />
     * latitude < 0 = S
     */
    private float latitude;
    /**
     * The longitude of this Building.<br />
     * longitude > 0 = E<br />
     * longitude < 0 = W
     */
    private float longitude;

    /**
     * Constructs a new Building object to hold information about a Building's
     * human-readable name, ID, latitude, and longitude.
     *
     * @param name The human-readable name of this Building.
     * @param id A three-character ID assigned to this Building.
     * @param latitude The latitude of this Building.
     * @param longitude The longitude of this Building.
     */
    public Building(String name, String id, float latitude, float longitude) {
        this.name = name;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return The human-readable name of this Building.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The three-character ID assigned to this Building.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The latitude of this Building.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @return The longitude of this Building.
     */
    public float getLongitude() {
        return longitude;
    }
}