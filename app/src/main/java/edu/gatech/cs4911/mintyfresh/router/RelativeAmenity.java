package edu.gatech.cs4911.mintyfresh.router;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;

/**
 * A RelativeAmenity class is an Amenity object wrapper with
 * natural ordering based on relative distance.
 */
public class RelativeAmenity implements Comparable<RelativeAmenity> {
    /**
     * The Amenity this RelativeAmenity refers to.
     */
    private Amenity amenity;
    /**
     * The relative distance between the user and this Amenity.
     */
    private double distance;

    /**
     * Constructs a new RelativeAmenity object, which is a
     * natural ordering wrapper around an Amenity object
     * based on relative distance to the user.
     *
     * @param amenity The Amenity this RelativeAmenity refers to.
     * @param distance The relative distance between the user and this Amenity.
     */
    public RelativeAmenity(Amenity amenity, double distance) {
        this.amenity = amenity;
        this.distance = distance;
    }

    /**
     * Compares this RelativeAmenity to another RelativeAmenity.<br />
     * This will return: <br /><br />
     * <b>-1</b> if this RelativeAmenity has a lower relative distance than the other.<br />
     * <b>0</b> if this RelativeAmenity has the same relative distance as the other.<br />
     * <b>1</b> if this RelativeAmenity has a greater relative distance than the other.
     *
     * @param altAmenity The RelativeAmenity to compare against.
     * @return A ranking based on relative distance (lower distance = higher ranking)
     */
    public int compareTo(RelativeAmenity altAmenity) {
        if (altAmenity.getDistance() < getDistance()) {
            // This Amenity is closer
            return -1;
        } else if (altAmenity.getDistance() > getDistance()) {
            // This Amenity is further away
            return 1;
        } else {
            // Amenities are in the same place
            return 0;
        }
    }

    /**
     * Returns the Amenity this RelativeAmenity refers to.
     *
     * @return The Amenity this RelativeAmenity refers to.
     */
    public Amenity getAmenity() {
        return amenity;
    }

    /**
     * Returns the relative distance between the user and this Amenity.
     *
     * @return The relative distance between the user and this Amenity.
     */
    public double getDistance() {
        return distance;
    }
}
