package edu.gatech.cs4911.mintyfresh;

import android.location.Location;

import java.util.List;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;
import edu.gatech.cs4911.mintyfresh.router.RelativeAmenity;
import edu.gatech.cs4911.mintyfresh.router.Router;

/**
 * AmenityFinder is an interface between the view
 * and the underlying query and filtering logic.
 */
public class AmenityFinder {
    /**
     * A packaged set of database connection details.
     */
    private DBHandler handler;

    /**
     * Constructs a new AmenityFinder with given database information.
     *
     * @param handler A DBHandler object, containing database connection details.
     */
    public AmenityFinder(DBHandler handler) {
        this.handler = handler;
    }

    /**
     * Returns a heap of all RelativeAmenity objects, ordered by relative distance.
     *
     * @param location The current location.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getNearbyAmenities(Location location)
            throws NoDbResultException {
        try {
            return heapPackager(location, DBQuery.getAmenities(handler));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a heap of all RelativeAmenity objects of a given type, ordered by relative distance.
     *
     * @param location The current location.
     * @param type The type of Amenity object.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getNearbyAmenitiesByType(Location location, String type)
            throws NoDbResultException {
        try {
            return heapPackager(location, DBQuery.getAmenitiesByType(handler, type));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a heap of all RelativeAmenity objects in a given Building,
     * ordered by relative distance.
     *
     * @param building A given building.
     * @param location The current location.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getAmenitiesInBuilding(Building building,
            Location location) throws NoDbResultException {
        try {
            return heapPackager(location, DBQuery.getAmenitiesByBuildingId(handler,
                    building.getId()));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a heap of all RelativeAmenity objects of a given type in a given Building,
     * ordered by relative distance.
     *
     * @param building A given building.
     * @param location The current location.
     * @param type The type of Amenity object.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getAmenitiesInBuilding(Building building,
            Location location, String type) throws NoDbResultException {
        try {
            return heapPackager(location, DBQuery.getAmenities(handler,
                    building.getId(), type));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a heap of all RelativeAmenity objects of a given type in a given Building,
     * ordered by relative distance.
     *
     * @param building A given building.
     * @param location The current location.
     * @param type The type of Amenity object.
     * @param level The floor to filter results by.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getAmenitiesInBuilding(Building building,
            Location location, String type, int level) throws NoDbResultException {
        try {
            return heapPackager(location, DBQuery.getAmenities(handler,
                    building.getId(), type, level));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Calculates relative distances between a given location and a set of Amenity objects,
     * and packages them into a PriorityQueue to rank by closest distance.
     *
     * @param location The current location.
     * @param dbResult A result list of Amenity objects returned from the database.
     * @return A ranked PriorityQueue of RelativeAmenity objects, sorted by closest distance.
     */
    private PriorityQueue<RelativeAmenity> heapPackager(Location location, List<Amenity> dbResult) {
        PriorityQueue<RelativeAmenity> output = new PriorityQueue<>();

        // Calculate relative distances and rank
        for (Amenity amenity : dbResult) {
            output.add(new RelativeAmenity(amenity, Router.calcRelativeDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    amenity.getLatitude(),
                    amenity.getLongitude())));
        }

        return output;
    }
}
