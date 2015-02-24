package edu.gatech.cs4911.mintyfresh.db.queryresponse;

import java.util.ArrayList;
import java.util.List;

/**
 * An Amenity holds information about an amenity's building, type, level, id,
 * latitude, longitude, attributes, and (x, y) floor plan location.
 */
public class Amenity extends DBResponseObject {
    /**
     * The Building parent that contains this Amenity.
     */
    private Building building;
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
     * A set of attributes which apply to this Amenity.
     */
    private List<String> attributes;
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
     * @param building The Building parent that contains this Amenity.
     * @param type The type of amenity.
     * @param level The floor this Amenity is located on.
     * @param id The unique identifier of this Amenity.
     * @param attributes A set of attributes which apply to this Amenity.
     * @param x This Amenity's x-location on a floorplan image.
     * @param y This Amenity's y-location on a floorplan image.
     */
    public Amenity(Building building, String type, int level, String id,
                   List<String> attributes, int x, int y) {
        this.building = building;
        this.type = type;
        this.level = level;
        this.id = id;
        this.attributes = attributes;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Amenity object to hold information about an amenity's building,
     * type, level, id, latitude, longitude, attributes, and (x, y) floor plan location.
     *
     * This constructor constructs a new list of attributes, then adds the one provided
     * to the new list.
     *
     * @param building The Building parent that contains this Amenity.
     * @param type The type of amenity.
     * @param level The floor this Amenity is located on.
     * @param id The unique identifier of this Amenity.
     * @param attribute An attribute which applies to this Amenity.
     * @param x This Amenity's x-location on a floorplan image.
     * @param y This Amenity's y-location on a floorplan image.
     */
    public Amenity(Building building, String type, int level, String id,
                   String attribute, int x, int y) {
        this(building, type, level, id, new ArrayList<String>(), x, y);

        addAttribute(attribute);
    }

    /**
     * Constructs a new Amenity object to hold information about an amenity's building,
     * type, level, id, latitude, longitude, attributes, and (x, y) floor plan location.
     *
     * This constructor constructs a new list of attributes in lieu of being provided one.
     *
     * @param building The Building parent that contains this Amenity.
     * @param type The type of amenity.
     * @param level The floor this Amenity is located on.
     * @param id The unique identifier of this Amenity.
     * @param x This Amenity's x-location on a floorplan image.
     * @param y This Amenity's y-location on a floorplan image.
     */
    public Amenity(Building building, String type, int level, String id,
                   int x, int y) {
        this(building, type, level, id, new ArrayList<String>(), x, y);
    }

    /**
     * Adds a new attribute to this Amenity's attribute list.
     *
     * @param attribute A new attribute to associate with this Amenity.
     */
    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    /**
     * Returns the three-character ID of the parent building.
     *
     * @return The three-character ID of the parent building.
     */
    public String getBuildingId() {
        return building.getId();
    }

    /**
     * Returns the human-readable full name of the parent building.
     *
     * @return The human-readable full name of the parent building.
     */
    public String getBuildingName() {
        return building.getName();
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
     * Returns this Amenity's attributes.
     *
     * @return This Amenity's attributes.
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * Updates this Amenity's x-location on a floorplan image.
     *
     * @param x This Amenity's new x-location on a floorplan image.
     */
    public void setX(int x) {
        this.x = x;
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
     * Updates this Amenity's y-location on a floorplan image.
     *
     * @param y This Amenity's new y-location on a floorplan image.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns this Amenity's y-location on a floorplan image.
     *
     * @return This Amenity's y-location on a floorplan image.
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Amenity) {
            if (((Amenity) object).getId().equals(getId())) {
                return true;
            }
        }

        return false;
    }
}