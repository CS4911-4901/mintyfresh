package edu.gatech.cs4911.mintyfresh;

import android.location.Location;

import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
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

    // TODO: Make specific exception types (e.g. Not found, nothing nearby, no way to route...)

    /**
     * Returns a heap of RelativeAmenity objects, ordered by relative distance.
     *
     * @param location The current location.
     * @return A heap of nearby amenities.
     * @throws Exception if any problem occurs (TODO: Specify our own exceptions)
     */
    public PriorityQueue<RelativeAmenity> getNearbyAmenities(Location location) throws Exception {
        PriorityQueue<RelativeAmenity> output = new PriorityQueue<>();

        // Query the database and calculate relative distances
        for (Amenity amenity : DBQuery.getAmenities(handler)) {
            output.add(new RelativeAmenity(amenity, Router.calcRelativeDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    amenity.getLatitude(),
                    amenity.getLongitude())));
        }

        return output;
    }
}
