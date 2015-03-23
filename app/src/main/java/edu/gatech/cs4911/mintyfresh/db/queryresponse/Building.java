package edu.gatech.cs4911.mintyfresh.db.queryresponse;

/**
 * A Building holds information about a nearby building's id, name, latitude, and longitude.
 */
public class Building extends DBResponseObject {
    /**
     * A three-character ID assigned to this Building.
     */
    private String id;
    /**
     * The human-readable name of this Building.
     */
    private String name;
    /**
     * The latitude of this Building.<br />
     * latitude > 0 = N<br />
     * latitude < 0 = S
     */
    private double latitude;
    /**
     * The longitude of this Building.<br />
     * longitude > 0 = E<br />
     * longitude < 0 = W
     */
    private double longitude;

    /**
     * Constructs a new Building object to hold information about a Building's
     * human-readable name, ID, latitude, and longitude.
     *
     * @param id A three-character ID assigned to this Building.
     * @param name The human-readable name of this Building.
     * @param latitude The latitude of this Building.
     * @param longitude The longitude of this Building.
     */
    public Building(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return The three-character ID assigned to this Building.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The human-readable name of this Building.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The latitude of this Building.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return The longitude of this Building.
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Building) {
            if (((Building) object).getId().equals(getId())) {
                return true;
            }
        }

        return false;
    }
}