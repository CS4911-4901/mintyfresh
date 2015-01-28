package edu.gatech.cs4911.mintyfresh.queryresponse;

/**
 * An Amenity holds information about an amenity's building, type, level, id,
 * latitude, longitude, attributes, and (x, y) floor plan location.
 */
public class Amenity extends DBResponseObject {
    /**
     * The three-character ID of the parent building.
     */
    private String buildingId;
    /**
     * The human-readable full name of the parent building.
     */
    private String buildingName;
    /**
     * The type of amenity.
     */
    private String type;
    /**
     * The floor this Amenity is located on.
     */
    private int level;
    /**
     * The unique identifier of this Amenity.
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
     * A set of attributes which apply to this Amenity.
     */
    private String[] attributes;
    /**
     * This Amenity's x-location on a floorplan image.
     */
    private int x;
    /**
     * This Amenity's y-location on a floorplan image.
     */
    private int y;

    /**
     * Constructs a new Amenity object to hold information about an amenity's building,
     * type, level, id, latitude, longitude, attributes, and (x, y) floor plan location.
     *
     * @param buildingId The three-character ID of the parent building.
     * @param buildingName The human-readable full name of the parent building.
     * @param type The type of amenity.
     * @param level The floor this Amenity is located on.
     * @param id The unique identifier of this Amenity.
     * @param latitude The latitude of this Building.
     * @param longitude The longitude of this Building.
     * @param attributes A set of attributes which apply to this Amenity.
     * @param x This Amenity's x-location on a floorplan image.
     * @param y This Amenity's y-location on a floorplan image.
     */
    public Amenity(String buildingId, String buildingName, String type, int level, String id,
                   float latitude, float longitude, String[] attributes, int x, int y) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.type = type;
        this.level = level;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.attributes = attributes;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the three-character ID of the parent building.
     *
     * @return The three-character ID of the parent building.
     */
    public String getBuildingId() {
        return buildingId;
    }

    /**
     * Returns the human-readable full name of the parent building.
     *
     * @return The human-readable full name of the parent building.
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * Returns this Amenity's type.
     *
     * @return This Amenity's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the floor this Amenity is located on.
     *
     * @return The floor this Amenity is located on.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the unique identifier of this Amenity.
     *
     * @return The unique identifier of this Amenity.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns this Amenity's latitude.
     *
     * @return This Amenity's latitude.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Returns this Amenity's longitude.
     *
     * @return This Amenity's longitude.
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Returns this Amenity's attributes.
     *
     * @return This Amenity's attributes.
     */
    public String[] getAttributes() {
        return attributes;
    }

    /**
     * Returns this Amenity's x-location on a floorplan image.
     *
     * @return This Amenity's x-location on a floorplan image.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns this Amenity's y-location on a floorplan image.
     *
     * @return This Amenity's y-location on a floorplan image.
     */
    public int getY() {
        return y;
    }
}