package edu.gatech.cs4911.mintyfresh.router;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;

/**
 * A RelativeBuilding class is an Building object wrapper with
 * natural ordering based on relative distance.
 */
public class RelativeBuilding implements Comparable<RelativeBuilding> {
    /**
     * The Building this RelativeBuilding refers to.
     */
    private Building building;
    /**
     * The relative distance between the user and this Building.
     */
    private double distance;

    /**
     * Constructs a new RelativeBuilding object, which is a
     * natural ordering wrapper around an Building object
     * based on relative distance to the user.
     *
     * @param building The Building this RelativeBuilding refers to.
     * @param distance The relative distance between the user and this Building.
     */
    public RelativeBuilding(Building building, double distance) {
        this.building = building;
        this.distance = distance;
    }

    /**
     * Compares this RelativeBuilding to another RelativeBuilding.<br />
     * This will return: <br /><br />
     * <b>-1</b> if this RelativeBuilding has a lower relative distance than the other.<br />
     * <b>0</b> if this RelativeBuilding has the same relative distance as the other.<br />
     * <b>1</b> if this RelativeBuilding has a greater relative distance than the other.
     *
     * @param altBuilding The RelativeBuilding to compare against.
     * @return A ranking based on relative distance (lower distance = higher ranking)
     */
    public int compareTo(RelativeBuilding altBuilding) {
        if (altBuilding.getDistance() < getDistance()) {
            // This Building is closer
            return -1;
        } else if (altBuilding.getDistance() > getDistance()) {
            // This Building is further away
            return 1;
        } else {
            // Amenities are in the same place
            return 0;
        }
    }

    /**
     * Returns the Building this RelativeBuilding refers to.
     *
     * @return The Building this RelativeBuilding refers to.
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Returns the relative distance between the user and this Building.
     *
     * @return The relative distance between the user and this Building.
     */
    public double getDistance() {
        return distance;
    }
}
